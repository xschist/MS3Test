package sample;

import com.univocity.parsers.common.IterableResult;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.nio.file.StandardOpenOption;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Controller {

    @FXML
    private TextField txtCsvLoc;

    @FXML
    private TextField txtDbLoc;

    @FXML
    private AnchorPane mainBoxContainer;
    @FXML
    private VBox mainBox;




    @FXML
    public void viewLogs(){

    }

    //csv locator
    @FXML
    public void openCsvFile(){
        try{
            Stage stage = (Stage) mainBoxContainer.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select CSV File:");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files","*.csv"));

            File newFile = fileChooser.showOpenDialog(stage);
            txtCsvLoc.setText(newFile.getAbsolutePath());
            }

        catch (Exception e) {

        }
    }

    //db locator
    @FXML
    public void dbLocator(){

        try{
            Stage stage = (Stage) mainBoxContainer.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select DB File:");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("SQLite Database Files","*.db","*.sqlite", "*.db3", "*.sqlite3"));

            File newFile = fileChooser.showOpenDialog(stage);
            txtDbLoc.setText(newFile.getAbsolutePath());
        }

        catch (Exception e) {

        }
    }
    

    @FXML
    //parses the csv file etc etc
    public void parseCSV() throws UnsupportedEncodingException, FileNotFoundException {

        //univocity settings
        CsvParserSettings settings = new CsvParserSettings();
        settings.setLineSeparatorDetectionEnabled(true);
        settings.getFormat().setLineSeparator("\n");
        settings.setHeaderExtractionEnabled(false);
        CsvParser parser = new CsvParser(settings);

        //select csv from directory
        String path = "";
        try{
            path = txtCsvLoc.getText().replace("\\","/");

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Location");
            alert.setContentText("Please specify a valid location");
        }

        try{

            //create new csv for invalid records
            String newFilePath = path.replace(".csv","-bad.csv");
            BufferedReader reader = new BufferedReader(new FileReader(path));
            //System.out.println("read "+newFilePath);

            int invalidCounter= 0;
            int validCounter = 0;
            int colCount = 10;

            //univocity writer
            CsvWriterSettings cSettings = new CsvWriterSettings();
            CsvWriter writer = new CsvWriter(new FileWriter(new File(newFilePath)), cSettings);


            IterableResult<String[], ParsingContext> row = parser.iterate(reader);
            settings.setHeaderExtractionEnabled(false);
            for(String[] roww :row ){
                for(int count = 0; count <colCount; count++){
                    //writes to _invalid records csv and logs
                    if(roww[count] == null){

                        invalidCounter++;
                        System.out.println(invalidCounter+ " "+Arrays.toString(roww));
                        writer.writeRow(roww); break;
                    }
                    if(count == (colCount-1)){

                        for(int i = count+1; i < roww.length; i++){
                            if(!(roww[i] == null)){
                                invalidCounter++;
                                System.out.println(invalidCounter+ "has extra "+Arrays.toString(roww));
                                writer.writeRow(roww); break;
                            }
                        }
                    }

                    //writes to sql database
                    else{validCounter++;}
                }

            }
            writer.close();

        }
        catch (FileNotFoundException e){
           /* Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid CSV File");
            alert.setContentText("CSV File not found/invalid file");
            alert.showAndWait();*/
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
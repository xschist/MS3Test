package sample;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
    private VBox mainBox;

    @FXML
    public void uploadCSV() throws UnsupportedEncodingException, FileNotFoundException {

        //univocity parser settings
        CsvParserSettings settings = new CsvParserSettings();
        settings.setLineSeparatorDetectionEnabled(true);
        settings.getFormat().setLineSeparator("\n");
        settings.setHeaderExtractionEnabled(false);
        CsvParser parser = new CsvParser(settings);
        //

        LocalDateTime dateTime = LocalDateTime.now();
        String dateTimeFormatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy hh-mm-ss-a"));

        try{
            //select csv from directory
           // String path = "D:/Earl/MS3/ms3Interviewsmol.csv";
            String path = openFile();
            //create new csv for invalid records
            String newFilePath = path.replace(".csv","_Invalid Records ("+dateTimeFormatted+").csv");
            BufferedReader reader = new BufferedReader(new FileReader(path));
            System.out.println("read");

            int invalidCounter= 0;
            int validCounter = 0;

            //univocity writer
            CsvWriterSettings cSettings = new CsvWriterSettings();
            CsvWriter writer = new CsvWriter(new FileWriter(new File(newFilePath)), cSettings);


            for(String[] row : parser.iterate(reader)){
                    for(int count = 0; count<10; count++){

                        //writes to _invalid records csv and logs
                        if(row[count] == null){
                            invalidCounter++;
                            System.out.println(invalidCounter+ " "+Arrays.toString(row));
                            writer.writeRow(row); break;

                        }

                        //writes to sql database
                        else{validCounter++;}
                    }

            }
            writer.close();

        }
        catch (FileNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid CSV File");
            alert.setContentText("CSV File not Consumed");

            alert.showAndWait();
        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @FXML
    public void viewLogs(){

    }

    public String openFile(){
        try{
            Stage stage = (Stage) mainBox.getScene().getWindow();
           // stage.initModality(Modality.WINDOW_MODAL);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select CSV File:");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files","*.csv"));

            File newFile = fileChooser.showOpenDialog(stage);
           return newFile.getAbsolutePath().replace("\\","/");}

        catch (Exception e) {

        }
        return null;
    }

    public int columnCounter(String[] row){
        int colCount = 0;



        return colCount;
    }
}
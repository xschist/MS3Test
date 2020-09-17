package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class Controller {

    @FXML
    private TextField txtCsvLoc;

    @FXML
    private TextField txtDbLoc;

    @FXML
    private AnchorPane mainBoxContainer;


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

       CsvParss pars = new CsvParss();
        try {
            pars.csvParsar(txtCsvLoc.getText(), txtDbLoc.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
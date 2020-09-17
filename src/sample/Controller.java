package sample;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.tools.Tool;
import java.io.*;
import java.sql.SQLException;

public class Controller {

    @FXML
    private TextField txtCsvLoc;
    @FXML
    private CheckBox createDbCheck;
    @FXML
    private TextField txtDbLoc;
    @FXML
    private AnchorPane mainBoxContainer;
    @FXML
    private Label txtFile;


    @FXML
    public void viewLogs() {

    }

    //csv locator
    @FXML
    public void openCsvFile() {
        try {
            Stage stage = (Stage) mainBoxContainer.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select CSV File:");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

            File newFile = fileChooser.showOpenDialog(stage);
            txtCsvLoc.setText(newFile.getAbsolutePath());
            txtFile.setText(newFile.getName());
            System.out.println(txtFile.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //db locator
    @FXML
    public void dbLocator() {

        try {
            Stage stage = (Stage) mainBoxContainer.getScene().getWindow();

            //db chosen
            if (!createDbCheck.isSelected()) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select DB File:");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("SQLite Database Files", "*.db", "*.sqlite", "*.db3", "*.sqlite3"));

                File newFile = fileChooser.showOpenDialog(stage);
                txtDbLoc.setText(newFile.getAbsolutePath());
            } else if (createDbCheck.isSelected()) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File file = directoryChooser.showDialog(stage);
                txtDbLoc.setText(file.getAbsolutePath()+"\\New Database.db");
            }
        } catch (Exception e) {

        }
    }


    @FXML
    //parses the csv file etc etc
    public void parseCSV() throws UnsupportedEncodingException, FileNotFoundException {

        CsvParss pars = new CsvParss();
        try {
            pars.csvParsar(txtCsvLoc.getText(), txtDbLoc.getText(), txtFile.getText());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

    public void setTxtDbLoc(String loc){
        txtDbLoc.setText(loc);
    }

    public String getFileName(File file){
        return file.getName();
    }

}
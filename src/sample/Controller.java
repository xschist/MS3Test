package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.tools.Tool;
import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.sql.SQLException;

public class Controller {

    @FXML
    private Button btnDbLocator;
    @FXML
    private TextField txtCsvLoc;
    @FXML
    private TextField txtDbLoc;
    @FXML
    private AnchorPane mainBoxContainer;
    @FXML
    private Label txtFile;
    @FXML
    public void viewLogs() throws IOException {
        Desktop.getDesktop().open(new File("db/"));
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
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File file = directoryChooser.showDialog(stage);
            txtDbLoc.setText(file.getAbsolutePath());

        } catch (Exception e) {

        }
    }
    @FXML
    //parses the csv file etc etc
    public void parseCSV() throws UnsupportedEncodingException, FileNotFoundException {

        CsvParss pars = new CsvParss();
        try {
          //  System.out.println("DBPATH: "+txtDbLoc.getText()+"/"+txtFile.getText());
            pars.csvParsar(txtCsvLoc.getText(), txtDbLoc.getText()+"\\"+txtFile.getText(), txtFile.getText());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }
}
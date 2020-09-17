package sample;

import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

import java.sql.*;

import java.io.*;
import java.util.List;


public class CsvWorkers {

    Connection conn;



    //extracts headers, removes nulls idk
    public String[] headerExtractor(String[] rowYourBoat){

        int count = 0;
        for(int i = 0; i<rowYourBoat.length; i++){
              if (rowYourBoat[i] ==null){
                  break;
              }
            count = i+1;
        }
        String[] headers = new String[count];

        for(int i = 0; i<count; i++){
            headers[i] = rowYourBoat[i];
        }

        return headers;
    }

    //identifies records with missing/extra entries then writes them to <input-filename>-bad.csv (same directory)
    public int[] badRecordFinder(List<String[]> allRows, int maxColumn, int columnCount, String path, String fileName)
            throws IOException, SQLException {

        //bunch of univocity csv settings
        CsvParserSettings parserSettings = new CsvParserSettings();
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setLineSeparatorDetectionEnabled(true);
        parserSettings.setProcessor(rowProcessor);
        parserSettings.setMaxCharsPerColumn(-1);
        parserSettings.setHeaderExtractionEnabled(true);
        parserSettings.setNullValue(null);
        CsvWriterSettings cSettings = new CsvWriterSettings();
        String newFilePath = path.replace(".csv","-bad.csv");
        CsvWriter writer = new CsvWriter(new FileWriter(new File(newFilePath)), cSettings);

        boolean foundBadRecord;
        int[] count = new int[2];
        for (String[]row:allRows) {

            foundBadRecord = false;

            for (int i = 0; i < columnCount; i++) {

                if (row[i] == null) {
                    count[0] += 1;
                    writer.writeRow(row);
                    foundBadRecord = true;
                    break;
                }


                if (i == columnCount - 1) {

                    for (int ii = i + 1; ii < row.length; ii++) {

                        if ((row[ii] != null)) {
                            writer.writeRow(row);
                            count[0] += 1;
                            foundBadRecord = true;
                            break;
                        }
                    }
                }
            }

            if(foundBadRecord==false){
                count[1]+=1;
                insertToDb(row, fileName);
            }

        }

        writer.close();
        return count;
    }

    //info alert
    public void alert(String dbLocation, String csvLocation, String badCsvLocation, int[] counters){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Finished Parsing!");
        alert.initStyle(StageStyle.UTILITY);
        alert.setContentText("File parsed: "+csvLocation+".\n"+
                        "Found "+counters[0]+" invalid entries out of "+(counters[0]+counters[1])+" records.\n"+
                        "Written invalid records to "+badCsvLocation.replace("/","\\")+".\n"+
                        "Written "+counters[1]+" valid records to "+dbLocation+".\n");

        alert.showAndWait();
    }


    public static void connectToDb(String dbNameLoc, String fileName) {

        //will connect to existing db
        //will create if no db is found
        String url = fileName.replace("\\","/");
        if(!url.contains(".db") || !url.contains(".sqlite")){
            url += ".db";

            Controller contr = new Controller();
            contr.setTxtDbLoc(url);
        }
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();

                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertToDb(String[] row, String fileName) throws SQLException {
        Statement statement = conn.createStatement();
    }

}

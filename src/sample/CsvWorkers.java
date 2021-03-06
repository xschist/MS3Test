package sample;

import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

import java.awt.*;
import java.sql.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class CsvWorkers {


    //extracts headers, removes nulls idk
    public String[] headerExtractor(String[] rowYourBoat) {

        int count = 0;
        for (int i = 0; i < rowYourBoat.length; i++) {
            if (rowYourBoat[i] == null) {
                break;
            }
            count = i + 1;
        }
        String[] headers = new String[count];

        for (int i = 0; i < count; i++) {
            headers[i] = rowYourBoat[i];
        }

        return headers;
    }

    //identifies records with missing/extra entries then writes them to <input-filename>-bad.csv (same directory)
    public int[] badRecordFinder(String[] headers, List<String[]> allRows, int columnCount, String path,
                                 String fileName, String dbPath) throws IOException, SQLException {

        List<String[]> goodRows = new ArrayList<>();
        //bunch of univocity csv settings
        CsvParserSettings parserSettings = new CsvParserSettings();
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setLineSeparatorDetectionEnabled(true);
        parserSettings.setProcessor(rowProcessor);
        parserSettings.setHeaderExtractionEnabled(true);
        parserSettings.setNullValue(null);
        parserSettings.setNumberOfRowsToSkip(1);
        CsvWriterSettings cSettings = new CsvWriterSettings();
        String newFilePath = path.replace(".csv", "-bad.csv");
        CsvWriter writer = new CsvWriter(new FileWriter(new File(newFilePath)), cSettings);

        sqlWorkers worker = new sqlWorkers();

        boolean foundBadRecord;
        int[] count = new int[2];
        for (String[] row : allRows) {

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

            if (foundBadRecord == false) {
                count[1] += 1;
                goodRows.add(row);
            }

        }

        worker.insertToDb(headers,goodRows, fileName, dbPath);
        writer.close();
        writeToLog(count, fileName, path);
        return count;
    }

    //info alert
    public void alert(String dbLocation, String csvLocation, String badCsvLocation, int[] counters) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Finished Parsing!");
        alert.initStyle(StageStyle.UTILITY);
        alert.setContentText("File parsed: " + csvLocation + ".\n" +
                "Found " + counters[0] + " invalid entries out of " + (counters[0] + counters[1]) + " records.\n" +
                "Written invalid records to " + badCsvLocation.replace("/", "\\")
                        .replace(".csv", "-bad.csv") + ".\n" +
                "Written " + counters[1] + " valid records to " + dbLocation + ".\n"
        );

        alert.showAndWait();
    }

    public void writeToLog(int[] counter, String fileName, String path) throws IOException {
        LocalDateTime dateTime = LocalDateTime.now();

        File logFile = new File("db/"+fileName.replace(".csv", ".log "));
        FileWriter writer = new FileWriter(logFile, true);
        PrintWriter pWriter = new PrintWriter(writer);

        pWriter.println("File: "+path.replace("/","\\")+" "+ dateTime.format(DateTimeFormatter.ofPattern("MMM dd,yyyy hh:mm:ss a")));
        pWriter.println("Records received: "+(counter[0]+counter[1])+"");
        pWriter.println("Records successful: "+counter[1]);
        pWriter.println("Records failed: "+counter[0]);
        pWriter.println("-----------------------------");
        pWriter.close();
        Desktop.getDesktop().open(new File(logFile.getAbsolutePath()));
    }
}

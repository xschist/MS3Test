package sample;

import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.List;


public class CsvWorkers {

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
    public int[] badRecordFinder(List<String[]> allRows, int maxColumn, int columnCount, String path) throws IOException{

        //bunch of univocity csv settings
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setLineSeparatorDetectionEnabled(true);
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setProcessor(rowProcessor);
        parserSettings.setMaxCharsPerColumn(-1);
        parserSettings.setHeaderExtractionEnabled(true);
        parserSettings.setNullValue(null);
        CsvWriterSettings cSettings = new CsvWriterSettings();
        String newFilePath = path.replace(".csv","-bad.csv");
        CsvWriter writer = new CsvWriter(new FileWriter(new File(newFilePath)), cSettings);

        int[] count = new int[2];
        for (String[]row:allRows) {

            for (int i = 0; i < columnCount; i++) {
                if (row[i] == null) {
                    count[0] += 1;
                    writer.writeRow(row);
                    break;
                }


                if (i == columnCount - 1) {

                    for (int ii = i + 1; ii < row.length; ii++) {
                        if ((row[ii] != null)) {
                            writer.writeRow(row);
                            count[0] += 1;
                            break;

                        }
                    }
                }
            }
            count[1]+=1;
        }

        writer.close();
        count[1] = count[1]-count[0];
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
                        "Written "+counters[1]+" good records to "+dbLocation+".\n");

        alert.showAndWait();
    }
}

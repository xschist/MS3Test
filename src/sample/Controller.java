package sample;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import javafx.fxml.FXML;

import java.io.*;
import java.nio.file.StandardOpenOption;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Controller {

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
            String path = "D:/Earl/MS3/ms3Interviewsmol.csv";
            //create new csv for invalid records
            String newFilePath = path.replace(".csv","_Invalid Records ("+dateTimeFormatted+").csv");
            BufferedReader reader = new BufferedReader(new FileReader(path));
            System.out.println("read");

            int invalidCounter= 0;

            //univocity writer
            CsvWriterSettings cSettings = new CsvWriterSettings();
            CsvWriter writer = new CsvWriter(new FileWriter(new File(newFilePath)), cSettings);

            //writes to _invalid records csv and logs
            for(String[] row : parser.iterate(reader)){
                    for(int count = 0; count<10; count++){
                        if(row[count] == null){
                            invalidCounter++;
                            System.out.println(invalidCounter+ " "+Arrays.toString(row));
                            writer.writeRow(row); break;

                        }
                    }

            }
            writer.close();

        }
        catch (FileNotFoundException e){
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void viewLogs(){

    }
}

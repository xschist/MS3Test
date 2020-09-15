package sample;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import javafx.fxml.FXML;

import java.io.*;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;

public class Controller {

    @FXML
    public void uploadCSV() throws UnsupportedEncodingException, FileNotFoundException {

        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.setHeaderExtractionEnabled(false);
       // settings.setEmptyValue("<EMPTY>");
        settings.setNullValue("<EMPTY>");
        //settings.setMaxColumns(10);
        CsvParser parser = new CsvParser(settings);

//        InputStreamReader qwe = new InputStreamReader(this.getClass().getResourceAsStream("‪D:/Earl/MS3/ms3Interview.csv"), "UTF-8");
        try{
            String path = "D:/Earl/MS3/ms3Interview.csv";
            BufferedReader reader = new BufferedReader(new FileReader(path));
            System.out.println("read");
            //List<Record> allRecords = parser.parseAllRecords(reader);
            int a= 0;
            /*for(Record record : allRecords){
                System.out.print(record.getString("A"));
                System.out.print(record.getString("B"));
                System.out.print(record.getString("C"));
                System.out.print(record.getString("D"));
                System.out.print(record.getString("E"));
                System.out.print(record.getString("F"));
                System.out.print(record.getString("G"));
                System.out.print(record.getString("H"));
                System.out.print(record.getString("I"));
                System.out.print(record.getString("J"));
                System.out.println();

                //print(", Model: " + record.getString("model"));
                // println(", Price: " + record.getBigDecimal("price"));
                a++;
            }
            */

            for(String[] row : parser.iterate(reader)){

                //System.out.println(Arrays.toString(row));
                if((Arrays.toString(row).contains("<EMPTY>")) ||(Arrays.toString(row).contains("<NULL>"))){
                    a++;
                    System.out.println(a+ " "+Arrays.toString(row));
                }

            }
            //System.out.println(a);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();

        }

    }

    @FXML
    public void viewLogs(){

    }
}

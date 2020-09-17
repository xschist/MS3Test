package sample;

import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.*;
import java.util.Arrays;
import java.util.List;


public class CsvParss {

    CsvWorkers workers = new CsvWorkers();

    public void csvParsar(String csvPath, String dbpath) throws IOException {
        String newPath =csvPath.replace("\\","/");
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setLineSeparatorDetectionEnabled(true);
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setProcessor(rowProcessor);
        parserSettings.setHeaderExtractionEnabled(false);
        CsvParser parser = new CsvParser(parserSettings);

        int maxLength;
        int maxColumn;
        int[] logCounters;

        try {
            System.out.println(newPath);
            BufferedReader reader = new BufferedReader(new FileReader(csvPath));
            parser.parse(reader);
        }catch (Exception e){ System.out.println("Unable to read input" + e);e.printStackTrace();}

        String[] headers = rowProcessor.getHeaders();
        List<String[]> rows = rowProcessor.getRows();

        maxLength= headers.length;
        maxColumn = workers.headerExtractor(headers).length;

        logCounters = workers.badRecordFinder(rows,maxLength,maxColumn, csvPath);
        workers.alert(dbpath, csvPath, newPath, logCounters);
    }
}

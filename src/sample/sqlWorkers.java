package sample;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class sqlWorkers {


    public void connectToDb(String dbNameLoc, String fileName) {

        String url = dbNameLoc.replace("\\", "/");
        url = url.replace(".csv", ".db");
        System.out.println("URL: " + url);
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {

                DatabaseMetaData meta = conn.getMetaData();

                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("Connected");
                conn.close();
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertToDb(String[] headers, List<String[]> rows, String fileName, String url) throws SQLException {
        try {

            String jdbcCon = "jdbc:sqlite:";
            url = jdbcCon + url;
            url = url.replace("\\", "/");
            url = url.replace(".csv", ".db");
            System.out.println("URL: " + url);

            try (Connection conn = DriverManager.getConnection(url)) {

                if (conn != null) {

                    DatabaseMetaData meta = conn.getMetaData();
                    conn.setAutoCommit(false);
                    Statement sqlStatement = conn.createStatement();
                    meta.getDriverName();
                    String tempStatement ;

                    //creates table + columns if not exist

                    tempStatement = "create table if not exists '" + fileName+ "'(";
                    for (int i = 0; i < headers.length; i++) {
                        if (i == headers.length - 1) {
                            tempStatement += " '" + headers[i] + "'CHAR NOT NULL ";
                        } else {
                            tempStatement += " '" + headers[i] + "'CHAR NOT NULL, ";
                        }
                    }
                    tempStatement += ");";
                    sqlStatement.executeUpdate(tempStatement);
                    conn.commit();

                    tempStatement = "";

                    for (String[] row : rows) {
                        tempStatement += "INSERT INTO '" +fileName+ "' VALUES(";
                        for (int i = 0; i < headers.length; i++) {
                            if (i == headers.length - 1) {
                                tempStatement += "\""+row[i] + "\")";
                            }
                            else {
                                tempStatement +=  "\"" + row[i] + "\", ";
                            }
                        }
                        System.out.println(tempStatement);
                        sqlStatement.executeUpdate(tempStatement);
                        conn.commit();
                        tempStatement = "";

                    }
                    conn.close();
                }

            } catch (SQLException e) {
                System.out.println("error here: " + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
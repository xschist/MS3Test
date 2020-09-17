package sample;

import java.sql.*;

public class sqlWorkers {
    public void connectToDb(String dbNameLoc, String fileName) {
        
        String url = fileName.replace("\\", "/");
        if (!url.contains(".db") || !url.contains(".sqlite")) {
            url += ".db";
        }
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {

                DatabaseMetaData meta = conn.getMetaData();

                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
                conn.close();
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertToDb(String[] headers, String[] row, String fileName) throws SQLException {
        try {

            String createQuery = "CREATE TABLE " + fileName + "(ID integer primary key ";

            for (int i = 0; i < headers.length; i++) {
                createQuery += headers[i] + " TEXT NOT NULL,";
            }
            createQuery += ")";
            System.out.println(createQuery);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

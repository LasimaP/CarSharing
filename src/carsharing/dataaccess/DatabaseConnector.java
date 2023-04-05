package carsharing.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/";
    static final String USER = "sa";
    static final String PASS = "";
    private Connection conn = null;
    private String fileName;

    public DatabaseConnector(String fileName) {
        this.fileName = fileName;
    }

    public Connection getConnection() {
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // STEP 2: Open a connection
            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL + fileName, USER, PASS);
            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            //System.out.println("Closing Connection...");
            if (conn != null) conn.close();
            //System.out.println("Goodbye!");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}

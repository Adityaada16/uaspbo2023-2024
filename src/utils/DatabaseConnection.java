package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static Connection connection;

    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlite:himada.db";
            connection = DriverManager.getConnection(url);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    return connection;
    }
}

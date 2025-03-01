package org.example.currency.db;

import java.sql.*;

public class DBConnector {

    private static final String DB_URL = "jdbc:sqlite:db/currency.db";

    static {
        loadDriver();
        DBInitializer.initialize();
    }

    private static void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

}
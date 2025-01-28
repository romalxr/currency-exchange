package org.example.currency.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseInitializer {

    public static void initialize() {

        String schemaPath = "src/main/resources/init.sql";

        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement()) {

            String schema = new String(Files.readAllBytes(Paths.get(schemaPath)));

            stmt.executeUpdate(schema);
            System.out.println("Database initialized successfully!");
        } catch (IOException e) {
            System.out.println("Error reading schema file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Failed to initialize database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        initialize();
    }
}

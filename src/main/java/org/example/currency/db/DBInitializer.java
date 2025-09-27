package org.example.currency.db;

import java.sql.Connection;
import java.sql.Statement;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBInitializer {

    private static boolean initialized = false;

    public static void initialize() {

        if (initialized) return;
        initialized = true;

        String schemaPath = "/init.sql";

        try (Connection conn = DBConnector.connect();
             Statement stmt = conn.createStatement();
             InputStream in =
                     DBInitializer.class.getResourceAsStream(schemaPath)) {
            if (in == null) {
                throw new IllegalStateException("Файл init.sql не найден в resources");
            }
            String schema = new String(in.readAllBytes());
            stmt.executeUpdate(schema);
            log.info("Database initialized successfully!");
        } catch (IOException e) {
            log.error("Error reading schema file: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Failed to initialize database: {}", e.getMessage(), e);
        }
    }
}

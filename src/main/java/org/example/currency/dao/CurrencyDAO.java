package org.example.currency.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAO {

    public List<String> getAllCurrencies() {
        List<String> currencies = new ArrayList<>();
        String query = "SELECT ID, Code, FullName FROM currencies";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String currency = String.format("{\"id\":\"%s\", \"code\":\"%s\", \"name\":\"%s\"}",
                        rs.getString("ID"), rs.getString("Code"), rs.getString("FullName"));
                currencies.add(currency);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencies;
    }

    public static void main(String[] args) {
        String json = "[" + String.join(",\n", new CurrencyDAO().getAllCurrencies()) + "]";
        System.out.println(json);
    }
}

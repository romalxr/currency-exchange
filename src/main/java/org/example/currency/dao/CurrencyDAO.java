package org.example.currency.dao;

import org.example.currency.db.DBConnector;
import org.example.currency.model.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAO {

    public List<Currency> findAll() {
        List<Currency> currencies = new ArrayList<>();
        String query = "SELECT ID, Code, FullName, Sign FROM currencies";

        try (Connection conn = DBConnector.connect();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                currencies.add(getCurrency(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencies;
    }

    public Currency findByCode(String code) {
        Currency currency = null;
        String query = "SELECT ID, FullName, Code, Sign FROM currencies WHERE Code = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, code);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    currency = getCurrency(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currency;
    }

    public Currency save(Currency currency) {
        String query = "INSERT INTO Currencies (FullName, Code, Sign) VALUES (?, ?, ?);";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, currency.getFullName());
            stmt.setString(2, currency.getCode());
            stmt.setString(3, currency.getSign());

            int changed = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return findByCode(currency.getCode());
    }

    public void delete(Currency currency) {
        String query = "DELETE FROM Currencies WHERE Id = ?;";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, currency.getId());
            int changed = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Currency getCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                    resultSet.getLong("id"),
                    resultSet.getString("fullName"),
                    resultSet.getString("code"),
                    resultSet.getString("sign"));
    }

    public Currency findById(long currencyId) {
        Currency currency = null;
        String query = "SELECT ID, FullName, Code, Sign FROM currencies WHERE ID = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, currencyId);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    currency = getCurrency(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currency;
    }
}

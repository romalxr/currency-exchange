package org.example.currency.dao;

import org.example.currency.db.DBConnector;
import org.example.currency.model.Currency;
import org.example.currency.model.ExchangeRate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDAO {

    public List<ExchangeRate> findAll() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String query = "SELECT ID, BaseCurrencyId, TargetCurrencyId, Rate FROM ExchangeRates";

        try (Connection conn = DBConnector.connect();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                exchangeRates.add(getExchangeRate(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exchangeRates;
    }

    public ExchangeRate findByBaseAndTarget(Currency currencyBase, Currency currencyTarget) {
        ExchangeRate exchangeRate = null;
        String query = "SELECT ID, BaseCurrencyId, TargetCurrencyId, Rate FROM ExchangeRates " +
                "WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, currencyBase.getId());
            stmt.setLong(2, currencyTarget.getId());

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    exchangeRate = getExchangeRate(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exchangeRate;
    }

    private static ExchangeRate getExchangeRate(ResultSet resultSet) throws SQLException {
        CurrencyDAO currencyDAO = new CurrencyDAO();

        return new ExchangeRate(
                resultSet.getLong("id"),
                currencyDAO.findById(resultSet.getLong("baseCurrencyId")),
                currencyDAO.findById(resultSet.getLong("targetCurrencyId")),
                resultSet.getFloat("rate"));
    }

    public ExchangeRate save(ExchangeRate exchangeRate) {
        String query = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?);";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, exchangeRate.getBaseCurrency().getId());
            stmt.setLong(2, exchangeRate.getTargetCurrency().getId());
            stmt.setFloat(3, exchangeRate.getRate());

            int changed = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return findByBaseAndTarget(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency());
    }

    public ExchangeRate update(ExchangeRate exchangeRate) {
        final String query = "UPDATE ExchangeRates SET BaseCurrencyId = ?, TargetCurrencyId = ?, Rate = ? WHERE ID = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, exchangeRate.getBaseCurrency().getId());
            stmt.setLong(2, exchangeRate.getTargetCurrency().getId());
            stmt.setFloat(3, exchangeRate.getRate());
            stmt.setLong(4, exchangeRate.getId());

            stmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return findById(exchangeRate.getId());
    }

    private ExchangeRate findById(Long id) {
        ExchangeRate exchangeRate = null;
        String query = "SELECT ID, BaseCurrencyId, TargetCurrencyId, Rate FROM ExchangeRates " +
                "WHERE ID = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    exchangeRate = getExchangeRate(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exchangeRate;
    }
}

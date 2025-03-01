package org.example.currency.controller.exchange;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency.dao.CurrencyDAO;
import org.example.currency.dao.ExchangeRateDAO;
import org.example.currency.model.Currency;
import org.example.currency.model.ExchangeRate;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private final ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();
    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        List<ExchangeRate> currencies = exchangeRateDAO.findAll();
        List<String> currenciesJson = currencies.stream().map(ExchangeRate::toJson).toList();
        String json = "[\n" + String.join(",\n", currenciesJson) + "\n]";
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        req.setCharacterEncoding("UTF-8");

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rateString = req.getParameter("rate");

        if (baseCurrencyCode == null || targetCurrencyCode == null || rateString == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters: baseCurrencyCode, targetCurrencyCode, or rate");
            return;
        }

        Currency currencyBase = currencyDAO.findByCode(baseCurrencyCode);
        if (currencyBase == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency not exist: " + baseCurrencyCode);
            return;
        }

        Currency currencyTarget = currencyDAO.findByCode(targetCurrencyCode);
        if (currencyTarget == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency not exist: " + targetCurrencyCode);
            return;
        }

        if (exchangeRateDAO.findByBaseAndTarget(currencyBase, currencyTarget) != null) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Exchange rate exist: " + baseCurrencyCode + targetCurrencyCode);
            return;
        }

        float rate = Float.parseFloat(rateString);
        ExchangeRate exchangeRate = new ExchangeRate(0L, currencyBase, currencyTarget, rate);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String json = exchangeRateDAO.save(exchangeRate).toJson();
        resp.getWriter().write(json);
    }
}

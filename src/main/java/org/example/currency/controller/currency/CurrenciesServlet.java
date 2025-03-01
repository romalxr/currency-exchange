package org.example.currency.controller.currency;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency.dao.CurrencyDAO;
import org.example.currency.model.Currency;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        List<Currency> currencies = currencyDAO.findAll();
        List<String> currenciesJson = currencies.stream().map(Currency::toJson).toList();
        String json = "[\n" + String.join(",\n", currenciesJson) + "\n]";
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        if (name == null || code == null || sign == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters: name, code, or sign");
            return;
        }

        if (currencyDAO.findByCode(code) != null) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Currency exist: " + code);
            return;
        }

        Currency currency = new Currency(0L, name, code, sign);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String json = currencyDAO.save(currency).toJson();
        resp.getWriter().write(json);
    }
}

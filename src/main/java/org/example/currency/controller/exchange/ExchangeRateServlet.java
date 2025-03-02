package org.example.currency.controller.exchange;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency.dao.CurrencyDAO;
import org.example.currency.dao.ExchangeRateDAO;
import org.example.currency.model.Currency;
import org.example.currency.dto.ErrorDTO;
import org.example.currency.model.ExchangeRate;
import org.example.currency.utils.Utils;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final CurrencyDAO currencyDAO = new CurrencyDAO();
    private final ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ErrorDTO.from("Currency code is missing"));
            return;
        }

        String currencyCodes = pathInfo.substring(1).toUpperCase();
        if (currencyCodes.length() < 6) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ErrorDTO.from("Currency code is wrong"));
            return;
        }

        String currencyCodeBase = currencyCodes.substring(0, 3);
        String currencyCodeTarget = currencyCodes.substring(3, 6);

        Currency currencyBase = currencyDAO.findByCode(currencyCodeBase);
        if (currencyBase == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, ErrorDTO.from("Currency code not found"));
            return;
        }
        Currency currencyTarget = currencyDAO.findByCode(currencyCodeTarget);
        if (currencyTarget == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, ErrorDTO.from("Currency code not found"));
            return;
        }

        ExchangeRate exchangeRate = exchangeRateDAO.findByBaseAndTarget(currencyBase, currencyTarget);
        if (exchangeRate == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, ErrorDTO.from("Exchange rate not found"));
            return;
        }
        String json = exchangeRate.toJson();
        resp.getWriter().write(json);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (method.equals("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String rateString = Utils.getParameterFromBody(req,"rate");
        if (rateString == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ErrorDTO.from("Missing parameters: rate"));
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ErrorDTO.from("Currency code is missing"));
            return;
        }

        String currencyCodes = pathInfo.substring(1).toUpperCase();
        if (currencyCodes.length() < 6) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ErrorDTO.from("Currency code is wrong"));
            return;
        }

        String currencyCodeBase = currencyCodes.substring(0, 3);
        String currencyCodeTarget = currencyCodes.substring(3, 6);

        Currency currencyBase = currencyDAO.findByCode(currencyCodeBase);
        if (currencyBase == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, ErrorDTO.from("Currency code not found: " + currencyCodeBase));
            return;
        }
        Currency currencyTarget = currencyDAO.findByCode(currencyCodeTarget);
        if (currencyTarget == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, ErrorDTO.from("Currency code not found " + currencyCodeTarget));
            return;
        }

        ExchangeRate exchangeRate = exchangeRateDAO.findByBaseAndTarget(currencyBase, currencyTarget);
        if (exchangeRate == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, ErrorDTO.from("Exchange rate not found"));
            return;
        }


        double rate = Double.parseDouble(rateString);
        exchangeRate.setRate(rate);
        exchangeRate = exchangeRateDAO.update(exchangeRate);

        String json = exchangeRate.toJson();
        resp.getWriter().write(json);
    }
}

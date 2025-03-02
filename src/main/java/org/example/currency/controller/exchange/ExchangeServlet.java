package org.example.currency.controller.exchange;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency.dao.CurrencyDAO;
import org.example.currency.dao.ExchangeRateDAO;
import org.example.currency.dto.ErrorDTO;
import org.example.currency.dto.ExchangeResultDTO;
import org.example.currency.model.Currency;
import org.example.currency.model.ExchangeRate;
import org.example.currency.utils.Utils;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private final ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();
    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String codeFrom = req.getParameter("from");
        String codeTo = req.getParameter("to");
        String amountString = req.getParameter("amount");

        if (codeFrom == null || codeTo == null || amountString == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ErrorDTO.from("Missing parameters: from, to, or amount"));
            return;
        }

        Double amount = Double.parseDouble(amountString);

        Currency currencyBase = currencyDAO.findByCode(codeFrom);
        if (currencyBase == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, ErrorDTO.from("Currency code not found: " + codeFrom));
            return;
        }
        Currency currencyTarget = currencyDAO.findByCode(codeTo);
        if (currencyTarget == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, ErrorDTO.from("Currency code not found " + codeTo));
            return;
        }

        ExchangeRate exchangeRate = exchangeRateDAO.findByBaseAndTarget(currencyBase, currencyTarget);
        if (exchangeRate == null) {
            ExchangeRate exchangeRateRev = exchangeRateDAO.findByBaseAndTarget(currencyTarget, currencyBase);
            if (exchangeRateRev != null && exchangeRateRev.getRate() != 0.0) {
                exchangeRate = new ExchangeRate(0L, currencyBase, currencyTarget, 1.0 / exchangeRateRev.getRate());
            }
        }

        if (exchangeRate == null) {
            Currency currencyUSD = currencyDAO.findByCode("USD");
            if (currencyUSD != null) {
                ExchangeRate usdA = exchangeRateDAO.findByBaseAndTarget(currencyUSD, currencyBase);
                ExchangeRate usdB = exchangeRateDAO.findByBaseAndTarget(currencyUSD, currencyTarget);

                if (usdA != null && usdB != null && usdA.getRate() != 0.0) {
                    exchangeRate = new ExchangeRate(0L, currencyBase, currencyTarget, usdB.getRate() / usdA.getRate());
                }
            }
        }

        if (exchangeRate == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, ErrorDTO.from("ExchangeServlet rate not found"));
            return;
        }

        double convertedAmount = Utils.round(amount * exchangeRate.getRate(), 2);
        ExchangeResultDTO exchangeResult = new ExchangeResultDTO(exchangeRate, amount, convertedAmount);
        String json = exchangeResult.toJson();
        resp.getWriter().write(json);
    }
}

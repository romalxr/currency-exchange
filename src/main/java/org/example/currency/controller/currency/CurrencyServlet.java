package org.example.currency.controller.currency;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency.dao.CurrencyDAO;
import org.example.currency.model.Currency;
import org.example.currency.dto.ErrorDTO;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ErrorDTO.from("Currency code is missing"));
            return;
        }

        String currencyCode = pathInfo.substring(1).toUpperCase();

        Currency currency = currencyDAO.findByCode(currencyCode);
        if (currency == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, ErrorDTO.from("Currency code not found"));
            return;
        }
        String json = currency.toJson();
        resp.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ErrorDTO.from("Currency code is missing"));
            return;
        }

        String currencyCode = pathInfo.substring(1).toUpperCase();

        Currency currency = currencyDAO.findByCode(currencyCode);
        currencyDAO.delete(currency);
        String json = "ok";
        resp.getWriter().write(json);
    }
}

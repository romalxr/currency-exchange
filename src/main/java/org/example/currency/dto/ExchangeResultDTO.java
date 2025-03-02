package org.example.currency.dto;

import org.example.currency.model.Currency;
import org.example.currency.model.ExchangeRate;

public class ExchangeResultDTO {
    private final Currency baseCurrency;
    private final Currency targetCurrency;
    private final double rate;
    private final double amount;
    private final double convertedAmount;

    public ExchangeResultDTO(ExchangeRate exchangeRate, double amount, double convertedAmount) {
        this.baseCurrency = exchangeRate.getBaseCurrency();
        this.targetCurrency = exchangeRate.getTargetCurrency();
        this.rate = exchangeRate.getRate();

        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public String toJson() {
        return "{\n" +
                "\"baseCurrency\" : " + baseCurrency.toJson() + ",\n" +
                "\"targetCurrency\" : " + targetCurrency.toJson() + ",\n" +
                "\"rate\" : " + rate + ",\n" +
                "\"amount\" : " + amount + ",\n" +
                "\"convertedAmount\" : " + convertedAmount + "\n" +
                "}";
    }
}

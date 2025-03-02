package org.example.currency.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    private Long id;

    private Currency baseCurrency;

    private Currency targetCurrency;

    private Double rate;

    public String toJson() {
        return String.format(
                "{\n\"id\": %d, \n\"baseCurrency\": %s, \n\"targetCurrency\": %s, \n\"rate\": \"%s\"\n}",
                this.getId(), this.getBaseCurrency().toJson(),
                this.getTargetCurrency().toJson(), this.getRate()
        );
    }
}

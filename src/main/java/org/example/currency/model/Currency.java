package org.example.currency.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    private Long id;

    @JsonProperty("name")
    private String fullName;

    private String code;

    private String sign;

    public String toJson() {
        return String.format(
                "{\n\"id\": %d, \n\"name\": \"%s\", \n\"code\": \"%s\", \n\"sign\": \"%s\"\n}",
                this.getId(), this.getFullName(), this.getCode(), this.getSign()
        );
    }
}

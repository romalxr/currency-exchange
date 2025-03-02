package org.example.currency.dto;

public class ErrorDTO {
    private final String message;

    public ErrorDTO(String message) {
        this.message = message;
    }

    public String toJson() {
        return "{\n" + "\"message\" : " + message + "\n}";
    }

    public static String from(String message) {
        ErrorDTO errorDTO = new ErrorDTO(message);
        return errorDTO.toJson();
    }
}

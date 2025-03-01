package org.example.currency.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;

public class Utils {

    public static String getParameterFromBody(HttpServletRequest req, String parameterName) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();

        String value = null;
        String[] pairs = data.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2 && parameterName.equals(keyValue[0])) {
                value = keyValue[1];
                break;
            }
        }
        return value;
    }

}

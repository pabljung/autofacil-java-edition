package com.autofacil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class DateParserUtil {

    private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
            DateTimeFormatter.ofPattern("ddMMyyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
    );

    public static LocalDate parseDataFlexivel(String dataStr) throws DateTimeParseException {
        dataStr = dataStr.trim();
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDate.parse(dataStr, formatter);
            } catch (DateTimeParseException e) {
                // tenta próximo formato
            }
        }
        throw new DateTimeParseException("Formato de data inválido", dataStr, 0);
    }
}

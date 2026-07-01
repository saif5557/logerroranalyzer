package com.saif.logerroranalyzer.util;

import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class DateTimeUtil {

    private static final DateTimeFormatter[] FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    };

    public LocalDateTime parseTimestamp(String timestampStr) {
        if (timestampStr == null || timestampStr.trim().isEmpty()) {
            return LocalDateTime.now();
        }

        // Clean the timestamp string
        String cleanTimestamp = timestampStr.trim();

        // Remove timezone info if present
        cleanTimestamp = cleanTimestamp.replaceAll("\\s+(IST|UTC|GMT).*", "");

        // Try to parse with different formatters
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDateTime.parse(cleanTimestamp, formatter);
            } catch (DateTimeParseException e) {
                // Continue to next formatter
            }
        }

        // If all parsing fails, returns current time
        return LocalDateTime.now();
    }
}

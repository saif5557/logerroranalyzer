package com.saif.logerroranalyzer.util;

import com.saif.logerroranalyzer.enums.LogLevel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LogPatternMatcher {

    // Predefined patterns for common log formats
    private static final Map<String, Pattern> LOG_PATTERNS = new HashMap<>();

    static {
        // Standard log pattern: LogLevel "Component" Message TimeStamp
        LOG_PATTERNS.put("STANDARD", Pattern.compile(
                "^(Debug|Warning|Critical|Fatal|Info|Error|ERROR)\\s*\"?([^\"\\s]+)\"?\\s*(.*?)\\s*(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}.*)?$"));

        // Simple pattern: LogLevel Message
        LOG_PATTERNS.put("SIMPLE", Pattern.compile(
                "^(Debug|Warning|Critical|Fatal|Info|Error|ERROR)\\s*:?\\s*(.+)$"));

        // Timestamp first pattern: Timestamp LogLevel Message
        LOG_PATTERNS.put("TIMESTAMP_FIRST", Pattern.compile(
                "^(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})\\s*(Debug|Warning|Critical|Fatal|Info|Error|ERROR)\\s*:?\\s*(.+)$"));

        // Timestamp Thread Level pattern: Timestamp [Thread] Level Component - Message
        // 2026-01-19 19:00:01,868 [1] INFO Core.Helpers.CommandInvoker - Execute
        // command : ...
        LOG_PATTERNS.put("TIMESTAMP_THREAD_LEVEL", Pattern.compile(
                "^(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}(?:,\\d{3})?)\\s+\\[(\\d+)\\]\\s+(INFO|DEBUG|WARNING|CRITICAL|FATAL|ERROR|Info|Warning|Critical|Fatal|Error|Debug)\\s+([^\\s]+)\\s+-\\s+(.+)$"));
    }

    private static final Pattern TIMESTAMP_PATTERN = Pattern.compile(
            "\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}(?:,\\d{3})?");

    private static final Pattern COMPONENT_PATTERN = Pattern.compile(
            "\"([^\"]+)\"");

    private static final Map<String, String> ERROR_CODE_PATTERNS = new HashMap<>();

    static {
        ERROR_CODE_PATTERNS.put("SIGNAL_6", "Signal:\\s*6|SIGABRT");
        ERROR_CODE_PATTERNS.put("DEVICE_NOT_OPEN", "device not open|port not open");
        ERROR_CODE_PATTERNS.put("CONNECTION_FAILED", "not connected|connection failed|connectToHost false");
        ERROR_CODE_PATTERNS.put("OPENTYPE_MISSING", "OpenType support missing");
        ERROR_CODE_PATTERNS.put("SIGNAL_NOT_FOUND", "no such signal");
        ERROR_CODE_PATTERNS.put("LASER_CONNECTION", "Laser is not connected");
        ERROR_CODE_PATTERNS.put("SERIAL_PORT_ERROR", "setupSerial.*false|Serial.*error");
        ERROR_CODE_PATTERNS.put("SOCKET_ERROR", "SocketConnecting|Socket.*error");
        ERROR_CODE_PATTERNS.put("RMQ_ERROR", "RMQ.*error|RabbitMQ.*error");
    }

    public LogLevel extractLogLevel(String line) {
        for (Map.Entry<String, Pattern> entry : LOG_PATTERNS.entrySet()) {
            Matcher matcher = entry.getValue().matcher(line);
            if (matcher.matches()) {
                String patternName = entry.getKey();
                int groupIndex = 1;

                if ("TIMESTAMP_FIRST".equals(patternName)) {
                    groupIndex = 2; // Group 2 is Level
                } else if ("TIMESTAMP_THREAD_LEVEL".equals(patternName)) {
                    groupIndex = 3; // Group 3 is Level
                }
                // STANDARD: Group 1 is Level
                // SIMPLE: Group 1 is Level

                if (matcher.groupCount() >= groupIndex) {
                    String levelStr = matcher.group(groupIndex);
                    return LogLevel.fromString(levelStr);
                }
            }
        }
        return LogLevel.DEBUG; // Default
    }

    public String extractComponent(String line) {
        // Check new pattern first
        Pattern threadPattern = LOG_PATTERNS.get("TIMESTAMP_THREAD_LEVEL");
        Matcher threadMatcher = threadPattern.matcher(line);
        if (threadMatcher.matches()) {
            return threadMatcher.group(4); // Component is group 4
        }

        Matcher matcher = COMPONENT_PATTERN.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }

        // Try to extract from common patterns
        if (line.contains("PS sendCommand"))
            return "Power Source";
        if (line.contains("setupSerial"))
            return "Serial Port";
        if (line.contains("RMQ"))
            return "RabbitMQ";
        if (line.contains("PLC"))
            return "PLC";
        if (line.contains("Laser"))
            return "Laser";
        if (line.contains("PSMTE"))
            return "Power Source MTE";
        if (line.contains("RFMTE"))
            return "Reference MTE";

        return "System";
    }

    public String extractErrorCode(String line) {
        String lowerLine = line.toLowerCase();

        for (Map.Entry<String, String> entry : ERROR_CODE_PATTERNS.entrySet()) {
            Pattern pattern = Pattern.compile(entry.getValue(), Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(line).find()) {
                return entry.getKey();
            }
        }

        // Look for numeric error codes
        Pattern numericErrorPattern = Pattern.compile("ERROR\\s*(\\d+)|error\\s*(\\d+)|code\\s*(\\d+)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = numericErrorPattern.matcher(line);
        if (matcher.find()) {
            return "ERROR_" + (matcher.group(1) != null ? matcher.group(1) : matcher.group(2));
        }

        return null;
    }

    public String extractMessage(String line) {
        for (Map.Entry<String, Pattern> entry : LOG_PATTERNS.entrySet()) {
            Matcher matcher = entry.getValue().matcher(line);
            if (matcher.matches()) {
                String patternName = entry.getKey();
                int groupIndex = 3; // Default for STANDARD (Group 3), SIMPLE (Group 2), TIMESTAMP_FIRST (Group 3)

                if ("SIMPLE".equals(patternName)) {
                    groupIndex = 2;
                } else if ("TIMESTAMP_THREAD_LEVEL".equals(patternName)) {
                    groupIndex = 5; // Group 5 is Message
                }

                if (matcher.groupCount() >= groupIndex) {
                    return matcher.group(groupIndex).trim();
                }
            }
        }
        return line.trim();
    }

    public String extractTimestampString(String line) {
        // Try specific patterns first to get exact timestamp group
        Pattern threadPattern = LOG_PATTERNS.get("TIMESTAMP_THREAD_LEVEL");
        Matcher threadMatcher = threadPattern.matcher(line);
        if (threadMatcher.matches()) {
            return threadMatcher.group(1);
        }

        Matcher matcher = TIMESTAMP_PATTERN.matcher(line);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}

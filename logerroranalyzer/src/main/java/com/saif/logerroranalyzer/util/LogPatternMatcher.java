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
        LOG_PATTERNS.put("STANDARD",Pattern.compile(
                "^(Debug|Warning|Critical|Fatal|Info|Error)\\s*\"?([^\"\\s]+)\"?\\s*(.*?)\\s*(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}.*)?$"
        ));

        // Simple pattern: LogLevel Message
        LOG_PATTERNS.put("SIMPLE",Pattern.compile(
                "^(Debug|Warning|Critical|Fatal|Info|Error)\\s*:?\\s*(.+)$"
        ));

        // Timestamp first pattern: Timestamp LogLevel Message
        LOG_PATTERNS.put("TIMESTAMP_FIRST",Pattern.compile(
                "^(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})\\s*(Debug|Warning|Critical|Fatal|Info|Error)\\s*:?\\s*(.+)$"
        ));
    }

    private static final Pattern TIMESTAMP_PATTERN = Pattern.compile(
            "\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}"
    );

    private static final Pattern COMPONENT_PATTERN = Pattern.compile(
            "\"([^\"]+)\""
    );

    private static final Map<String, String> ERROR_CODE_PATTERNS = new HashMap<>();

    static {
        ERROR_CODE_PATTERNS.put("SIGNAL_6","Signal:\\s*6|SIGABRT");
        ERROR_CODE_PATTERNS.put("DEVICE_NOT_OPEN","device not open|port not open");
        ERROR_CODE_PATTERNS.put("CONNECTION_FAILED","not connected|connection failed|connectToHost false");
        ERROR_CODE_PATTERNS.put("OPENTYPE_MISSING","OpenType support missing");
        ERROR_CODE_PATTERNS.put("SIGNAL_NOT_FOUND","no such signal");
        ERROR_CODE_PATTERNS.put("LASER_CONNECTION","Laser is not connected");
        ERROR_CODE_PATTERNS.put("SERIAL_PORT_ERROR","setupSerial.*false|Serial.*error");
        ERROR_CODE_PATTERNS.put("SOCKET_ERROR","SocketConnecting|Socket.*error");
        ERROR_CODE_PATTERNS.put("RMQ_ERROR","RMQ.*error|RabbitMQ.*error");
    }

    public LogLevel extractLogLevel(String line){
        for(Pattern pattern: LOG_PATTERNS.values()){
            Matcher matcher = pattern.matcher(line);
            if(matcher.matches()){
                String levelStr = matcher.group(1);
                return LogLevel.fromString(levelStr);
            }
        }
        return LogLevel.DEBUG; // Default
    }

    public String extractComponent(String line){
        Matcher matcher = COMPONENT_PATTERN.matcher(line);
        if(matcher.find()){
            return matcher.group(1);
        }

        // Try to extract from common patterns
        if(line.contains("PS sendCommand")) return "Power Source";
        if(line.contains("setupSerial")) return "Serial Port";
        if(line.contains("RMQ")) return "RabbitMQ";
        if(line.contains("PLC")) return "PLC";
        if(line.contains("Laser")) return "Laser";
        if(line.contains("PSMTE")) return "Power Source MTE";
        if(line.contains("RFMTE")) return "Reference MTE";

        return "System";
    }

    public String extractErrorCode(String line){
        String  lowerLine = line.toLowerCase();

        for(Map.Entry<String,String> entry: ERROR_CODE_PATTERNS.entrySet()){
            Pattern pattern = Pattern.compile(entry.getValue(),Pattern.CASE_INSENSITIVE);
            if(pattern.matcher(line).find()){
                return entry.getKey();
            }
        }

        // Look for numeric error codes
        Pattern numericErrorPattern = Pattern.compile("error\\s*(\\d+)|code\\s*(\\d+)",Pattern.CASE_INSENSITIVE);
        Matcher matcher = numericErrorPattern.matcher(line);
        if(matcher.find()){
            return "ERROR_" + (matcher.group(1)!=null ? matcher.group(1):matcher.group(2));
        }

        return null;
    }

    public String extractMessage(String line){
        for(Pattern pattern : LOG_PATTERNS.values()){
            Matcher matcher = pattern.matcher(line);
            if(matcher.find()){
                //Get the message part (usually the last capturing group before timestamp)
                int groupCount = matcher.groupCount();
                if(groupCount>=3){
                    return matcher.group(3).trim();
                }
            }
        }
        return line.trim();
    }

    public String extractTimestampString(String line){
        Matcher matcher = TIMESTAMP_PATTERN.matcher(line);
        if(matcher.find()){
            return matcher.group();
        }
        return null;
    }
}

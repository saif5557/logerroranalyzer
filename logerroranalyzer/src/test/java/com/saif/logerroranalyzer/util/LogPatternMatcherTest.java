package com.saif.logerroranalyzer.util;

import com.saif.logerroranalyzer.enums.LogLevel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LogPatternMatcherTest {

    @Test
    public void testTimestampThreadLevelPattern() {
        LogPatternMatcher matcher = new LogPatternMatcher();
        String logLine = "2026-01-19 19:00:01,868 [1] INFO  Core.Helpers.CommandInvoker - Execute command : C:\\Program Files (x86)\\Renesas Electronics";

        LogLevel level = matcher.extractLogLevel(logLine);
        assertEquals(LogLevel.INFO, level, "Log Level should be INFO");

        String component = matcher.extractComponent(logLine);
        assertEquals("Core.Helpers.CommandInvoker", component, "Component should be extracted");

        String message = matcher.extractMessage(logLine);
        assertEquals("Execute command : C:\\Program Files (x86)\\Renesas Electronics", message,
                "Message should be extracted");

        String timestamp = matcher.extractTimestampString(logLine);
        assertEquals("2026-01-19 19:00:01,868", timestamp, "Timestamp should handle comma milliseconds");
    }

    @Test
    public void testErrorLogLine() {
        LogPatternMatcher matcher = new LogPatternMatcher();
        String logLine = "2026-01-19 19:01:50,828 [1] ERROR ZigLibrary.Manager.TempSensorBufferManager - Partial Data received at Temp Sensor Com port.";

        LogLevel level = matcher.extractLogLevel(logLine);
        assertEquals(LogLevel.ERROR, level);

        String component = matcher.extractComponent(logLine);
        assertEquals("ZigLibrary.Manager.TempSensorBufferManager", component);

        String message = matcher.extractMessage(logLine);
        assertEquals("Partial Data received at Temp Sensor Com port.", message);
    }
}

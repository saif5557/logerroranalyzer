package com.saif.logerroranalyzer.dto;

import com.saif.logerroranalyzer.enums.LogLevel;

import java.time.LocalDateTime;

public class LogEntryDto {
    private LocalDateTime timestamp;
    private LogLevel logLevel;
    private String component;
    private String message;
    private String errorCode;
    private String fullLine;
    private String fileName;

    //Constructors

    public LogEntryDto() {
    }

    public LogEntryDto(LocalDateTime timestamp, LogLevel logLevel, String component, String message, String errorCode, String fullLine) {
        this.timestamp = timestamp;
        this.logLevel = logLevel;
        this.component = component;
        this.message = message;
        this.errorCode = errorCode;
        this.fullLine = fullLine;
    }

    // Getters and Setters

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getFullLine() {
        return fullLine;
    }

    public void setFullLine(String fullLine) {
        this.fullLine = fullLine;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

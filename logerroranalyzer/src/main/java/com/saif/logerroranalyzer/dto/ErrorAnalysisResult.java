package com.saif.logerroranalyzer.dto;

import com.saif.logerroranalyzer.enums.ErrorSeverity;
import com.saif.logerroranalyzer.enums.ErrorType;
import com.saif.logerroranalyzer.enums.LogLevel;

import java.time.LocalDateTime;

public class ErrorAnalysisResult {
    private LocalDateTime timestamp;
    private String errorCode;
    private ErrorType errorType;
    private LogLevel logLevel;
    private String component;
    private String message;
    private String description;
    private String solution;
    private ErrorSeverity severity;
    private String fullLogLine;
    private String fileName;
    private boolean isMatched;
    private int count = 1;

    // Constructors

    public ErrorAnalysisResult() {
    }

    public ErrorAnalysisResult(LocalDateTime timestamp, String errorCode, ErrorType errorType, LogLevel logLevel,
            String component, String message, String description, String solution, ErrorSeverity severity,
            String fullLogLine, boolean isMatched) {
        this.timestamp = timestamp;
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.logLevel = logLevel;
        this.component = component;
        this.message = message;
        this.description = description;
        this.solution = solution;
        this.severity = severity;
        this.fullLogLine = fullLogLine;
        this.isMatched = isMatched;
    }

    // Getters and Setters

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public ErrorSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(ErrorSeverity severity) {
        this.severity = severity;
    }

    public String getFullLogLine() {
        return fullLogLine;
    }

    public void setFullLogLine(String fullLogLine) {
        this.fullLogLine = fullLogLine;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void incrementCount() {
        this.count++;
    }
}

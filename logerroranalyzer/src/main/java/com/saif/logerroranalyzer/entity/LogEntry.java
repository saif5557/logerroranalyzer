package com.saif.logerroranalyzer.entity;

import com.saif.logerroranalyzer.enums.LogLevel;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_entries")
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "log_level")
    private LogLevel logLevel;

    @Column(name = "component")
    private String component;

    @Column(name = "message", length = 2000)
    private String message;

    @Column(name = "full_line", length = 4000)
    private String fullLine;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "analysis_session")
    private String analysisSession;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //Constructors
    public LogEntry(){
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getFullLine() {
        return fullLine;
    }

    public void setFullLine(String fullLine) {
        this.fullLine = fullLine;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAnalysisSession() {
        return analysisSession;
    }

    public void setAnalysisSession(String analysisSession) {
        this.analysisSession = analysisSession;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

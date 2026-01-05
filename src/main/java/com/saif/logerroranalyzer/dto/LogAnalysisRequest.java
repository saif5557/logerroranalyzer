package com.saif.logerroranalyzer.dto;

public class LogAnalysisRequest {
    private String logContent;
    private String fileName;
    private boolean saveToDatebase;
    private String sessionId;

    // Constructors

    public LogAnalysisRequest() {
    }

    public LogAnalysisRequest(String logContent, String fileName) {
        this.logContent = logContent;
        this.fileName = fileName;
        this.saveToDatebase = false;
    }

    // Getters and Setters

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isSaveToDatebase() {
        return saveToDatebase;
    }

    public void setSaveToDatebase(boolean saveToDatebase) {
        this.saveToDatebase = saveToDatebase;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}

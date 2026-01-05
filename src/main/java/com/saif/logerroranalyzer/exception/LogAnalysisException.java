package com.saif.logerroranalyzer.exception;

public class LogAnalysisException extends RuntimeException{

    public LogAnalysisException(String message){
        super(message);
    }

    public LogAnalysisException(String message, Throwable cause){
        super(message, cause);
    }
}

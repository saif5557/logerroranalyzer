package com.saif.logerroranalyzer.enums;

public enum LogLevel {
    DEBUG("Debug"),
    INFO("Info"),
    WARNING("Warning"),
    ERROR("Error"),
    CRITICAL("Critical"),
    FATAL("Fatal");

    private final String displayName;

    LogLevel(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }

    public static LogLevel fromString(String text){
        for(LogLevel level: LogLevel.values()){
            if(level.displayName.equalsIgnoreCase(text) || level.name().equalsIgnoreCase(text)){
                return level;
            }
        }
        return DEBUG;
    }
}

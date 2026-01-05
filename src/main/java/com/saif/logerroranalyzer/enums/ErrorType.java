package com.saif.logerroranalyzer.enums;

public enum ErrorType {
    SYSTEM_ERROR("System Error"),
    HARDWARE_ERROR("Hardware Error"),
    NETWORK_ERROR("Network Error"),
    DATABASE_ERROR("Database Error"),
    CONFIGURATION_ERROR("Configuration Error"),
    VALIDATION_ERROR("Validation Error"),
    AUTHENTICATION_ERROR("Authentication Error"),
    PERMISSION_ERROR("Permission Error"),
    TIMEOUT_ERROR("Timeout Error"),
    MES_APPLICATION_ERROR("MES Application Error"),
    UNKNOWN_ERROR("Unknown Error");

    private final String displayName;

    ErrorType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

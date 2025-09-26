package com.saif.logerroranalyzer.enums;

public enum ErrorSeverity {
    LOW(1,"Low", "#28a745"),
    MEDIUM(2,"Medium","#ffc107"),
    HIGH(3,"High", "#fd7e14"),
    CRITICAL(4,"Critical","#dc3545");

    private final int priority;
    private final String displayName;
    private final String colorCode;

    ErrorSeverity(int priority,String displayName, String colorCode){
        this.priority = priority;
        this.displayName = displayName;
        this.colorCode = colorCode;
    }

    public int getPriority(){ return priority;}
    public String getDisplayName(){return displayName;}
    public String getColorCode(){return colorCode;}
}

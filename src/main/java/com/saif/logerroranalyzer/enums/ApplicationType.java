package com.saif.logerroranalyzer.enums;

public enum ApplicationType {
    MES("MES Application"),
    SPM("SPM Application"),
    CARTON_PACKING("CartonPacking Application"),
    METER_SCREENING_JIG("MeterScreening Jig Application");

    private final String displayName;

    ApplicationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

package es.gobcan.istac.coetl.domain.enumeration;

public enum LogLevel {

    ERROR("Error", "Error"), BASIC("Basic", "Basic"), DEBUG("Debug", "Debug");

    private String pentahoLogLevel;
    private String hopLogLevel;

    LogLevel(String pentahoLogLevel, String hopLogLevel) {
        this.pentahoLogLevel = pentahoLogLevel;
        this.hopLogLevel = hopLogLevel;
    }

    public String getPentahoLogLevel() {
        return pentahoLogLevel;
    }

    public String getHopLogLevel() {
        return hopLogLevel;
    }
}

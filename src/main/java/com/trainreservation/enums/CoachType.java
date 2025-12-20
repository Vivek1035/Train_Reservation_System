package com.trainreservation.enums;

public enum CoachType {
    AC_1A("1A - First AC"),
    AC_2A("2A - Second AC"),
    AC_3A("3A - Third AC"),
    SLEEPER("SL - Sleeper"),
    GENERAL("GS - General"),
    AC_CHAIR_CAR("CC - AC Chair Car"),
    SECOND_SEATING("2S - Second Seating");
    
    private final String description;
    
    CoachType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

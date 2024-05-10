package com.HotelApp.domain.entity.enums;

public enum CategoriesEnum {
    SINGLE("Single Room"),
    STUDIO("Studio"),
    DOUBLE("Double Room"),
    DELUXE("Deluxe Room"),
    PRESIDENT("Presidential Room");

    private final String displayValue;

    CategoriesEnum(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

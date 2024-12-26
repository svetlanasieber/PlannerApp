package com.plannerapp.model.entity;


public enum PriorityEnum {
    URGENT("Urgent"),
    IMPORTANT("Important"),
    LOW("Low");

    private final String value;

    private PriorityEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

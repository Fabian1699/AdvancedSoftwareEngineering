package com.example.taskforce.task;

public enum Frequency {
    ONETIME("einmalig"),
    DAY("täglich"),
    WEEK("wöchentlich"),
    MONTH("monatlich"),
    YEAR("jährlich"),
    CUSTOM("benutzerdefiniert in Tagen");

    private final String key;

    private Frequency(String key){
        this.key = key;
    }

    public String getKey(){
        return this.key;
    }
}

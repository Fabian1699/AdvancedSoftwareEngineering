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

    public static Frequency fromKey(String key) throws Exception {
        for(Frequency freq: Frequency.values()){
            if(freq.getKey().equals(key)){
                return freq;
            }
        }
        throw new Exception("Frequency key not found");
    }
}

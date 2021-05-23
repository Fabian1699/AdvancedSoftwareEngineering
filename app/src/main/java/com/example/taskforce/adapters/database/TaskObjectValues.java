package com.example.taskforce.adapters.database;

public enum TaskObjectValues {
    NAME("name"),
    ID("id"),
    TARGET_DATE("target_date"),
    FINISH_DATE("finish_date"),
    TIME_SPENT("time_spent"),
    FREQUENCY("frequency"),
    FINISHED("finished");

    private final String key;

    private TaskObjectValues(String key){
        this.key = key;
    }

    public String getKey(){
        return this.key;
    }

    public static TaskObjectValues fromKey(String key) throws Exception {
        for(TaskObjectValues value: TaskObjectValues.values()){
            if(value.getKey().equals(key)){
                return value;
            }
        }
        throw new Exception("TaskObjectValue key not found");
    }
}

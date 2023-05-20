package de.dhbw.fireinventory.domain.condition;

import java.util.ArrayList;
import java.util.List;

public enum Condition {
    FUNKTIONSFÄHIG("funktionsfähig"),
    NICHT_FUNKTIONSFÄHIG("nicht funktionsfähig"),
    IN_REPARATUR("in Reparatur");

    private String condition;

    private Condition(String condition){
        this.condition = condition;
    }

    public String getCondition(){
        return condition;
    }

    public static List<String> getAllConditions(){
        List<String> conditions = new ArrayList<>();
        for (Condition condition: values()) {
            conditions.add(condition.getCondition());
        }
        return conditions;
    }
}

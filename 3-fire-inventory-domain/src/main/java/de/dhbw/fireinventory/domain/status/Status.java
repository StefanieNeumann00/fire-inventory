package de.dhbw.fireinventory.domain.status;

import java.util.ArrayList;
import java.util.List;

public enum Status {
    EINSATZBEREIT("einsatzbereit"),
    VOR_ORT("vor Ort"),
    NICHT_VOR_ORT("nicht vor Ort"),
    IN_REPARATUR("in Reparatur"),
    KAPUTT("kaputt");

    private String designation;

    private Status(String status){
        this.designation = status;
    }

    public String getDesignation(){
        return designation;
    }

    public static List<String> getAll(){
        List<String> statuses = new ArrayList<>();
        for (Status status: values()) {
            statuses.add(status.getDesignation());
        }
        return statuses;
    }
}

package de.dhbw.fireinventory.domain.status;

import de.dhbw.fireinventory.domain.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "status")
public class Status extends AbstractEntity {
    private String designation;

    public Status() {

    }

    public Status(String name) {
        this.designation = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String name) {
        this.designation = name;
    }
}

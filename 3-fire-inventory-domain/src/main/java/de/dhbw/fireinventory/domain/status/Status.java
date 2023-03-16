package de.dhbw.fireinventory.domain.status;

import de.dhbw.fireinventory.domain.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "status")
public class Status extends AbstractEntity {

    @NotNull
    @NotEmpty
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

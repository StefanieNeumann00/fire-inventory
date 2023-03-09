package de.dhbw.fireinventory.domain.place;

import de.dhbw.fireinventory.domain.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "place")
public class Place extends AbstractEntity {

    @NotNull
    private String designation = "";

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}

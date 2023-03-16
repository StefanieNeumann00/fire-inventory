package de.dhbw.fireinventory.domain.vehicle;

import de.dhbw.fireinventory.domain.AbstractEntity;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.status.Status;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "vehicle")
public class Vehicle extends AbstractEntity {

    @NotNull
    @NotEmpty
    private String designation = "";

    @ManyToOne
    @NotNull
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    private Place place;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}



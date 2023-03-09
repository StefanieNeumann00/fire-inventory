package de.dhbw.fireinventory.domain.equipment;

import de.dhbw.fireinventory.domain.AbstractEntity;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "equipment")
public class Equipment extends AbstractEntity {

    @NotNull
    private String designation = "";

    @ManyToOne
    @NotNull
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

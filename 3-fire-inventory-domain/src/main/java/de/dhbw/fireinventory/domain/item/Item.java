package de.dhbw.fireinventory.domain.item;

import de.dhbw.fireinventory.domain.AbstractEntity;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.location.Location;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Item extends AbstractEntity {

    @ManyToOne
    @NotNull
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @NotNull
    private Status status;

    public Location getLocation() {
        return this.location;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

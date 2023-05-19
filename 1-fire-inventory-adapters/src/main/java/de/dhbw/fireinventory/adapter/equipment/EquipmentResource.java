package de.dhbw.fireinventory.adapter.equipment;

import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.location.Location;

public class EquipmentResource {

    private final String designation;

    private final Status status;

    private final Location location;

    public EquipmentResource(final String designation, final Status status, final Location location) {
        this.designation = designation;
        this.status = status;
        this.location = location;
    }

    public String getDesignation() { return designation; }

    public Status getStatus() {
        return status;
    }

    public Location getLocation() {
        return location;
    }
}

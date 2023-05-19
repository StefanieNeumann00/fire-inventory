package de.dhbw.fireinventory.adapter.vehicle;

import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.location.Location;

public class VehicleResource {

    private final String designation;

    private final Status status;

    private final Location location;

    public VehicleResource(final String designation, final Status status, final Location location) {
        this.designation = designation;
        this.status = status;
        this.location = location;
    }

    public String getDesignation() { return designation; }

    public Status getStatus() {
        return status;
    }

    public Location getPlace() {
        return location;
    }

}

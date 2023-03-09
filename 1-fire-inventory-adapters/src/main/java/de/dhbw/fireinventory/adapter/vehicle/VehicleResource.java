package de.dhbw.fireinventory.adapter.vehicle;

import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.status.Status;

public class VehicleResource {

    private final String designation;

    private final Status status;

    private final Place place;

    public VehicleResource(final String designation, final Status status, final Place place) {
        this.designation = designation;
        this.status = status;
        this.place = place;
    }

    public String getDesignation() { return designation; }

    public Status getStatus() {
        return status;
    }

    public Place getPlace() {
        return place;
    }

}

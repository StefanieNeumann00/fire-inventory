package de.dhbw.fireinventory.adapter.location;

import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

public class LocationResource {

    private final String designation;

    private final Vehicle vehicle;

    private final Place place;

    public LocationResource(final String designation, final Vehicle vehicle, final Place place) {
        this.designation = designation;
        this.vehicle = vehicle;
        this.place = place;
    }

    public String getDesignation() { return designation; }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Place getPlace() {
        return place;
    }

}

package de.dhbw.fireinventory.adapter.place;

import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

public class PlaceResource {

    private final String designation;

    public PlaceResource(final String designation) {
        this.designation = designation;
    }

    public String getDesignation() { return designation; }

}

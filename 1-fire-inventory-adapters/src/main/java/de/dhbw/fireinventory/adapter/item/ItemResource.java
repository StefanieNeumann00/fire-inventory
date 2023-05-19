package de.dhbw.fireinventory.adapter.item;

import de.dhbw.fireinventory.domain.location.Location;

public class ItemResource {

    private final String designation;

    private final Location location;

    public ItemResource(final String designation, final Location location) {
        this.designation = designation;
        this.location = location;
    }

    public String getDesignation() { return designation; }

    public Location getLocation() {
        return location;
    }
}

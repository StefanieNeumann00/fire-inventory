package de.dhbw.fireinventory.adapter.item;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

public class ItemResource {

    private final String designation;

    private final Vehicle vehicle;

    private final Equipment equipment;

    public ItemResource(final String designation, final Vehicle vehicle, final Equipment equipment) {
        this.designation = designation;
        this.vehicle = vehicle;
        this.equipment = equipment;
    }

    public String getDesignation() { return designation; }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Equipment getEquipment() {
        return equipment;
    }
}

package de.dhbw.fireinventory.adapter.appointment;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import java.time.LocalDate;
import java.util.Date;


public class AppointmentResource {

    private final String designation;

    private final Item item;

    private final LocalDate dueDate;

    public AppointmentResource(final String designation, final Item item, final LocalDate dueDate) {
        this.designation = designation;
        this.item = item;
        this.dueDate = dueDate;
    }

    public String getDesignation() {
        return designation;
    }

    public Item getItem() {
        return item;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}

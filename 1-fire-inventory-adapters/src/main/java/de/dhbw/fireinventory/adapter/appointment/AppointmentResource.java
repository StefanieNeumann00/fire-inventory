package de.dhbw.fireinventory.adapter.appointment;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import java.util.Date;


public class AppointmentResource {

    private final String designation;

    private final Vehicle vehicle;

    private final Equipment equipment;

    private final Date dueDate;

    private final Date endDate;

    private final int interval;

    public AppointmentResource(final String designation, final Vehicle vehicle, final Equipment equipment, final Date dueDate, final Date endDate, final int interval) {
        this.designation = designation;
        this.vehicle = vehicle;
        this.equipment = equipment;
        this.dueDate = dueDate;
        this.endDate = endDate;
        this.interval = interval;
    }

    public String getDesignation() { return designation; }

    public Equipment getEquipment() {
        return equipment;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Date getDueDate() { return dueDate; }

    public Date getEndDate() { return endDate; }

    public int getInterval() { return interval; }
}

package de.dhbw.fireinventory.domain.location;

import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

public class VehiclePlace extends Location {

    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    Vehicle vehicle;

    public void setVehicle(Vehicle vehicle){ this.vehicle = vehicle;}

    public Vehicle getVehicle() {return this.vehicle;}
}

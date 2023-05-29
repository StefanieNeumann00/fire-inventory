package de.dhbw.fireinventory.application.domain.service.vehiclePlace;

import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import java.util.UUID;

public class VehiclePlaceResource extends LocationResource {

    private final Vehicle vehicle;

    public VehiclePlaceResource(UUID id, String designation, Vehicle vehicle) {
        this.id = id;
        this.designation = designation;
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {return vehicle; }

}

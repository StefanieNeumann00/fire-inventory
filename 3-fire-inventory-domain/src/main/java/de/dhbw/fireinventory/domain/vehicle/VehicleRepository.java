package de.dhbw.fireinventory.domain.vehicle;

import de.dhbw.fireinventory.domain.location.Location;

import java.util.List;

public interface VehicleRepository {

    List<Vehicle> findAllVehicles();

    Vehicle save(Vehicle vehicle);
}

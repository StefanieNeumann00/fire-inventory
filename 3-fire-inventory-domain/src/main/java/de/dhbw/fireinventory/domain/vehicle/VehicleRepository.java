package de.dhbw.fireinventory.domain.vehicle;

import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;

import java.util.List;

public interface VehicleRepository {

    List<Vehicle> findAllVehicles();

    Vehicle save(Vehicle vehicle);

    int vehicleStatusCount(String status);
}

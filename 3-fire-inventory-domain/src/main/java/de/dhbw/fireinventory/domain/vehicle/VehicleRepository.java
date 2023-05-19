package de.dhbw.fireinventory.domain.vehicle;

import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.location.Location;

import java.util.List;

public interface VehicleRepository {

    List<Vehicle> findAllBy(String filterText);

    Vehicle save(Vehicle vehicle);

    void delete(Vehicle vehicle);

    int vehicleStatusCount(Status status);

    List<Vehicle> findAllByLocation(Location location);

    List<Vehicle> findAllByLocationAndDesignation(Location location, String designation);

    List<Vehicle> findAllByLocationAndStatus(Location location, Status status);

    List<Vehicle> findAllByLocationStatusAndDesignation(Location location, Status status, String designation);

    List<Vehicle> findAllByStatus(Status status);

    List<Vehicle> findAllByStatusAndDesignation(Status status, String designation);
}

package de.dhbw.fireinventory.domain.location;

import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import java.util.List;

public interface LocationRepository {

    Location save(Location location);

    void delete(Location location);

    List<Location> findAllBy(String filterText);

    List<Location> findAll();

    VehiclePlace findLocationForVehicle(Vehicle vehicle);

    boolean hasLinkedItems(Location location);

    boolean isPresent(Location location);
}

package de.dhbw.fireinventory.domain.location;

import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import java.util.List;

public interface LocationRepository {

    List<Location> findAllLocations();

    Location save(Location location);

    Location getLocationForVehicle(Vehicle vehicle);

    Location getLocationForPlace(Place place);
}

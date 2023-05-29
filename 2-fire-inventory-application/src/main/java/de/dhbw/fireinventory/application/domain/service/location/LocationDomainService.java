package de.dhbw.fireinventory.application.domain.service.location;

import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

public interface LocationDomainService extends LocationDomainServicePort {

    Location findLocationForVehicle(Vehicle vehicle);

    void deleteLocation(Location location);

    boolean hasLinkedItems(Location location);
}

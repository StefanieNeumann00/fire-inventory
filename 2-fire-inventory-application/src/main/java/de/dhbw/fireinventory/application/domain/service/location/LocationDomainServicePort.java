package de.dhbw.fireinventory.application.domain.service.location;

import de.dhbw.fireinventory.domain.location.Location;

import java.util.List;

public interface LocationDomainServicePort {

    void saveLocation(Location location);

    List<Location> findAllLocations(String filterText);

    List<Location> findAllPlaces(String filterText);
}

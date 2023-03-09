package de.dhbw.fireinventory.domain.location;

import java.util.List;

public interface LocationRepository {

    List<Location> findAllLocations();

    Location save(Location location);
}

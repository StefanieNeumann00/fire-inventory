package de.dhbw.fireinventory.domain.location;

import java.util.List;

public interface LocationRepository {

    Location save(Location location);

    void delete(Location location);

    List<Location> findAll(String filterText);
}

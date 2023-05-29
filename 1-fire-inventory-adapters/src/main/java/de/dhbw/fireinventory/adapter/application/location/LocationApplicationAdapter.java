package de.dhbw.fireinventory.adapter.application.location;

import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import java.util.List;

public interface LocationApplicationAdapter {

    void saveLocation(LocationResource locationResource);

    void deleteLocation(LocationResource locationResource);

    List<LocationResource> findAllLocations(String filterText);

    List<LocationResource> findAllPlaces(String filterText);
}

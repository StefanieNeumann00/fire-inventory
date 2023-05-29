package de.dhbw.fireinventory.application.mediator.service.location;

import de.dhbw.fireinventory.domain.location.Location;

public interface LocationServicePort {

    boolean deleteLocation(Location location);
}

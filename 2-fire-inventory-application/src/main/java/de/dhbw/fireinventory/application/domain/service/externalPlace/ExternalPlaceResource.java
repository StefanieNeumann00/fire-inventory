package de.dhbw.fireinventory.application.domain.service.externalPlace;

import de.dhbw.fireinventory.application.domain.service.location.LocationResource;

import java.util.UUID;

public class ExternalPlaceResource extends LocationResource {

    public ExternalPlaceResource() {}

    public ExternalPlaceResource(UUID id, final String designation) {
        this.id = id;
        this.designation = designation;
    }

}

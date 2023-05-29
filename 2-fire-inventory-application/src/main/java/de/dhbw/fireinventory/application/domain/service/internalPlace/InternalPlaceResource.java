package de.dhbw.fireinventory.application.domain.service.internalPlace;

import de.dhbw.fireinventory.application.domain.service.location.LocationResource;

import java.util.UUID;

public class InternalPlaceResource extends LocationResource {

    public InternalPlaceResource() {}

    public InternalPlaceResource(UUID id, String designation) {
        this.id = id;
        this.designation = designation;
    }

}

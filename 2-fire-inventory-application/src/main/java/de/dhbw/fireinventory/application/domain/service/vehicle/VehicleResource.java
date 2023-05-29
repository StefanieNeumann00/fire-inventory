package de.dhbw.fireinventory.application.domain.service.vehicle;

import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.status.Status;

import java.util.UUID;

public class VehicleResource extends ItemResource {

    public VehicleResource() {}

    public VehicleResource(UUID id, String designation, Condition condition, Status status, LocationResource locationResource) {
        this.id = id;
        this.designation = designation;
        this.condition = condition;
        this.status = status;
        this.locationResource = locationResource;
    }
}

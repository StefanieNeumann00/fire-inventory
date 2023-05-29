package de.dhbw.fireinventory.application.domain.service.item;

import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.status.Status;

import java.util.UUID;

public class ItemResource {

    protected String designation;

    protected Condition condition;

    protected LocationResource locationResource;

    protected Status status;

    protected UUID id;

    public UUID getId() { return id; }

    public String getDesignation() { return designation; }

    public Condition getCondition() {
        return condition;
    }

    public LocationResource getLocationResource() {
        return locationResource;
    }

    public Status getStatus() {return status; }

    public void setDesignation(String designation) { this.designation = designation; }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setLocationResource(LocationResource locationResource) {
        this.locationResource = locationResource;
    }

    public void setStatus(Status status) { this.status = status; }

    public void setId(UUID id) { this.id = id; }
}

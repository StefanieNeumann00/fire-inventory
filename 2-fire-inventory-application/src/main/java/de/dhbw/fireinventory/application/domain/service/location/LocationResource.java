package de.dhbw.fireinventory.application.domain.service.location;

import java.util.UUID;

public abstract class LocationResource {

    protected String designation;

    protected UUID id;

    public String getDesignation() { return this.designation; }

    public UUID getId() { return id; }

    public void setDesignation(String designation) {this.designation = designation; }

    public void setId(UUID id) { this.id = id; }
}

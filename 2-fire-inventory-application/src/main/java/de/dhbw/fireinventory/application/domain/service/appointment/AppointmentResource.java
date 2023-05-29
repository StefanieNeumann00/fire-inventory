package de.dhbw.fireinventory.application.domain.service.appointment;

import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.fireinventory.domain.item.Item;

import java.time.LocalDate;
import java.util.UUID;


public class AppointmentResource {

    private String designation;

    private ItemResource itemResource;

    private LocalDate dueDate;

    private UUID id;

    public AppointmentResource() {}

    public AppointmentResource(UUID id, String designation, ItemResource itemResource, LocalDate dueDate) {
        this.id = id;
        this.designation = designation;
        this.itemResource = itemResource;
        this.dueDate = dueDate;
    }

    public String getDesignation() {
        return designation;
    }

    public ItemResource getItemResource() {
        return itemResource;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public UUID getId() { return id; }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setItemResource(ItemResource itemResource) {
        this.itemResource = itemResource;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setId(UUID id) { this.id = id; }
}

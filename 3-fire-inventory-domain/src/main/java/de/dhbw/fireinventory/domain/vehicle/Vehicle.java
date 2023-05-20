package de.dhbw.fireinventory.domain.vehicle;

import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.status.StatusDeterminator;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Vehicle")
public class Vehicle extends Item {

    @Override
    public Status determineStatus(Condition condition) {
        return StatusDeterminator.determineVehicleStatus(this.getLocation(), condition);
    }
}



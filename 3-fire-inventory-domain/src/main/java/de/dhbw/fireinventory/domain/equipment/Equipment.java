package de.dhbw.fireinventory.domain.equipment;

import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.status.StatusDeterminator;

import javax.persistence.*;


@Entity
@DiscriminatorValue("Equipment")
public class Equipment extends Item {

    @Override
    public Status determineStatus(Condition condition) {
        return StatusDeterminator.determineEquipmentStatus(this.getLocation(), condition);
    }
}

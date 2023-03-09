package de.dhbw.fireinventory.adapter.equipment;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EquipmentToEquipmentResourceMapper implements Function<Equipment, EquipmentResource> {

    @Override
    public EquipmentResource apply(final Equipment equipment) {
        return map(equipment);
    }

    private EquipmentResource map(final Equipment equipment) {
        return new EquipmentResource(equipment.getDesignation(), equipment.getStatus(), equipment.getLocation());
    }
}

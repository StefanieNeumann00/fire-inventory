package de.dhbw.fireinventory.adapter.mapper.item;

import de.dhbw.fireinventory.adapter.mapper.location.LocationToLocationResourceMapper;
import de.dhbw.fireinventory.application.domain.service.equipment.EquipmentResource;
import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.fireinventory.application.domain.service.vehicle.VehicleResource;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.item.Item;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ItemToItemResourceMapper implements Function<Item, ItemResource> {

    LocationToLocationResourceMapper locationToLocationResourceMapper = new LocationToLocationResourceMapper();

    @Override
    public ItemResource apply(final Item item) {
        return map(item);
    }

    private ItemResource map(final Item item) {
        if (item instanceof Equipment) {
            return new EquipmentResource(item.getId(), item.getDesignation(), item.getCondition(), item.getStatus(), locationToLocationResourceMapper.apply(item.getLocation()));
        }
        else {
            return new VehicleResource(item.getId(), item.getDesignation(), item.getCondition(), item.getStatus(), locationToLocationResourceMapper.apply(item.getLocation()));
        }
    }
}

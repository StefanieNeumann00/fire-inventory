package de.dhbw.fireinventory.adapter.mapper.item;

import de.dhbw.fireinventory.adapter.mapper.location.LocationResourceToLocationMapper;
import de.dhbw.fireinventory.application.domain.service.equipment.EquipmentResource;
import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.fireinventory.application.domain.service.vehicle.VehicleResource;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Component
public class ItemResourceToItemMapper implements Function<ItemResource, Item> {

    LocationResourceToLocationMapper locationResourceToLocationMapper = new LocationResourceToLocationMapper();

    @Override
    public Item apply(final ItemResource itemResource) {
        return map(itemResource);
    }

    private Item map(final ItemResource itemResource) {

        if (itemResource instanceof EquipmentResource) {
            EquipmentResource equipmentResource = (EquipmentResource)itemResource;
            Equipment equipment = new Equipment();
            equipment.setId(equipment.getId());
            equipment.setDesignation(equipmentResource.getDesignation());
            equipment.setCondition(equipmentResource.getCondition());
            equipment.setLocation(locationResourceToLocationMapper.apply(equipmentResource.getLocationResource()));

            return equipment;
        }
        else if (itemResource instanceof VehicleResource){
            VehicleResource vehicleResource = (VehicleResource)itemResource;
            Vehicle vehicle = new Vehicle();
            vehicle.setId(vehicleResource.getId());
            vehicle.setDesignation(vehicleResource.getDesignation());
            vehicle.setCondition(vehicleResource.getCondition());
            vehicle.setLocation(locationResourceToLocationMapper.apply(vehicleResource.getLocationResource()));

            return vehicle;
        }
        else { return null; }
    }
}

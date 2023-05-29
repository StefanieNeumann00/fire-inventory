package de.dhbw.fireinventory.adapter.application.equipment;

import de.dhbw.fireinventory.adapter.mapper.item.ItemResourceToItemMapper;
import de.dhbw.fireinventory.adapter.mapper.item.ItemToItemResourceMapper;
import de.dhbw.fireinventory.adapter.mapper.location.LocationResourceToLocationMapper;
import de.dhbw.fireinventory.application.domain.service.equipment.EquipmentResource;
import de.dhbw.fireinventory.application.domain.service.item.ItemDomainServicePort;
import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.application.mediator.service.item.ItemServicePort;
import de.dhbw.fireinventory.domain.status.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentAppAdapter implements EquipmentApplicationAdapter {

    private final ItemDomainServicePort itemDomainServicePort;
    private final ItemServicePort itemServicePort;
    private final ItemToItemResourceMapper itemToItemResourceMapper;
    private final ItemResourceToItemMapper itemResourceToItemMapper;
    private final LocationResourceToLocationMapper locationResourceToLocationMapper;

    @Override
    public List<ItemResource> findAllEquipments(String designation) {
        return this.itemDomainServicePort.findAllEquipments(designation).stream()
                .map(itemToItemResourceMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResource> findAllEquipmentsBy(LocationResource locationResource, Status status, String designation) {
        return this.itemDomainServicePort.findAllEquipmentsBy(locationResourceToLocationMapper.apply(locationResource), status, designation).stream()
                .map(itemToItemResourceMapper)
                .collect(Collectors.toList());
    }

    @Override
    public void saveEquipment(EquipmentResource equipmentResource) {
        this.itemServicePort.save(itemResourceToItemMapper.apply((ItemResource) equipmentResource), null);
    }

    @Override
    public void changeCondition(EquipmentResource equipmentResource) {
        this.itemDomainServicePort.changeCondition(itemResourceToItemMapper.apply(equipmentResource));
    }

    @Override
    public void deleteEquipment(EquipmentResource equipmentResource) {
        this.itemServicePort.delete(itemResourceToItemMapper.apply((ItemResource)equipmentResource));
    }

    @Override
    public int getEquipmentCount() {
        return this.itemDomainServicePort.getEquipmentCount();
    }

    @Override
    public int equipmentStatusCount(Status status) {
        return this.itemDomainServicePort.equipmentStatusCount(status);
    }

}

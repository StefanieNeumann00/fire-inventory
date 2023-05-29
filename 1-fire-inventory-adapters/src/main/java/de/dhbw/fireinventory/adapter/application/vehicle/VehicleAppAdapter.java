package de.dhbw.fireinventory.adapter.application.vehicle;

import de.dhbw.fireinventory.adapter.mapper.item.ItemResourceToItemMapper;
import de.dhbw.fireinventory.adapter.mapper.item.ItemToItemResourceMapper;
import de.dhbw.fireinventory.adapter.mapper.location.LocationResourceToLocationMapper;
import de.dhbw.fireinventory.application.domain.service.item.ItemDomainServicePort;
import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.application.domain.service.vehicle.VehicleResource;
import de.dhbw.fireinventory.application.mediator.service.item.ItemServicePort;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.status.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleAppAdapter implements VehicleApplicationAdapter {

    private final ItemDomainServicePort itemDomainServicePort;
    private final ItemServicePort vehicleServicePort;
    private final ItemResourceToItemMapper itemResourceToItemMapper;
    private final ItemToItemResourceMapper ItemToItemResourceMapper;
    private final LocationResourceToLocationMapper locationResourceToLocationMapper;

    @Override
    public List<ItemResource> findAllVehicles(String filterText) {
        return this.itemDomainServicePort.findAllVehicles(filterText).stream()
                .map(ItemToItemResourceMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResource> findAllVehiclesBy(LocationResource locationResource, Status status, String designation) {
        return this.itemDomainServicePort.findAllVehiclesBy(locationResourceToLocationMapper.apply(locationResource), status, designation).stream()
                .map(ItemToItemResourceMapper)
                .collect(Collectors.toList());
    }

    @Override
    public void saveVehicle(VehicleResource vehicleResource, Condition condition) {
        this.vehicleServicePort.save(this.itemResourceToItemMapper.apply(vehicleResource), condition);
    }

    @Override
    public void deleteVehicle(VehicleResource vehicleResource) {
        this.vehicleServicePort.delete(this.itemResourceToItemMapper.apply(vehicleResource));
    }

    @Override
    public void changeCondition(VehicleResource vehicleResource) {
        this.itemDomainServicePort.changeCondition(this.itemResourceToItemMapper.apply(vehicleResource));
    }

    @Override
    public int getVehicleCount() {
        return this.itemDomainServicePort.getVehicleCount();
    }

    @Override
    public int vehicleStatusCount(Status status) {
        return this.itemDomainServicePort.vehicleStatusCount(status);
    }

}

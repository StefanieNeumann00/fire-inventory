package de.dhbw.fireinventory.adapter.application.equipment;

import de.dhbw.fireinventory.application.domain.service.equipment.EquipmentResource;
import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.domain.status.Status;

import java.util.List;

public interface EquipmentApplicationAdapter {

    List<ItemResource> findAllEquipments(String designation);

    List<ItemResource> findAllEquipmentsBy(LocationResource locationResource, Status status, String designation);

    void saveEquipment(EquipmentResource equipmentResource);

    void changeCondition(EquipmentResource equipmentResource);

    void deleteEquipment(EquipmentResource equipmentResource);

    int getEquipmentCount();

    int equipmentStatusCount(Status status);
}

package de.dhbw.fireinventory.application.domain.service.item;

import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;

import java.util.List;

public interface ItemDomainServicePort {

    List<Item> findAllItems();

    List<Item> findAllVehicles(String filterText);

    List<Item> findAllVehiclesBy(Location location, Status status, String designation);

    void changeCondition(Item item);

    int getVehicleCount();

    int vehicleStatusCount(Status status);

    List<Item> findAllEquipments(String designation);

    List<Item> findAllEquipmentsBy(Location location, Status status, String designation);

    int getEquipmentCount();

    int equipmentStatusCount(Status status);
}

package de.dhbw.fireinventory.domain.item;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import java.util.List;

public interface ItemRepository {

    List<Item> findAllItems();

    Item save(Item item);

    Item getItemForVehicle(Vehicle vehicle);

    Item getItemForEquipment(Equipment equipment);

    void deleteItem(Item item);
}

package de.dhbw.fireinventory.application.item;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.item.ItemRepository;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.LocationRepository;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemApplicationService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemApplicationService(final ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findAllItems() {
        return this.itemRepository.findAllItems();
    }

    public void saveItem(Item item){ this.itemRepository.save(item); }

    public Item getItemForVehicle(Vehicle vehicle) { return this.itemRepository.getItemForVehicle(vehicle); }

    public Item getItemForEquipment(Equipment equipment) { return this.itemRepository.getItemForEquipment(equipment); }

    public void deleteItem(Equipment equipment) {
        Item item = this.getItemForEquipment(equipment);
        this.itemRepository.deleteItem(item);
    }

    public void deleteItem(Vehicle vehicle) {
        Item item = this.getItemForVehicle(vehicle);
        this.itemRepository.deleteItem(item);
    }
}

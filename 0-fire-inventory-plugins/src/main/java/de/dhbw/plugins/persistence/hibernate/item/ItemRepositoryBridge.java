package de.dhbw.plugins.persistence.hibernate.item;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.item.ItemRepository;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import de.dhbw.plugins.persistence.hibernate.location.SpringDataLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepositoryBridge implements ItemRepository {

    private final SpringDataItemRepository springDataItemRepository;

    @Autowired
    public ItemRepositoryBridge(final SpringDataItemRepository springDataItemRepository) {
        this.springDataItemRepository = springDataItemRepository;
    }

    @Override
    public List<Item> findAllItems() {
        return this.springDataItemRepository.findAll();
    }

    @Override
    public Item save(final Item item) {
        return this.springDataItemRepository.save(item);
    }

    @Override
    public Item getItemForVehicle(Vehicle vehicle) {
        return this.springDataItemRepository.getItemForVehicle(vehicle);
    }

    @Override
    public Item getItemForEquipment(Equipment equipment) {
        return this.springDataItemRepository.getItemForEquipment(equipment);
    }

    @Override
    public void deleteItem(Item item) {
        this.springDataItemRepository.delete(item);
    }
}

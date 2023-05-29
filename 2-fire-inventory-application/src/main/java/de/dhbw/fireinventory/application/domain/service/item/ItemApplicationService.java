package de.dhbw.fireinventory.application.domain.service.item;

import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.item.ItemRepository;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemApplicationService implements ItemDomainService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemApplicationService(final ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findAllItems() { return this.itemRepository.findAll(); }

    @Override
    public List<Item> findAllVehicles(String filterText) {
        return this.filterVehicle(this.itemRepository.findAllBy(filterText));
    }

    @Override
    public List<Item> findAllVehiclesBy(Location location, Status status, String designation) {
        if (location == null && status == null) { return this.findAllVehicles(designation); }
        else if (location != null && status == null && designation.isEmpty()) { return this.filterVehicle(this.itemRepository.findAllByLocation(location)); }
        else if (location != null && status == null && !designation.isEmpty()) { return this.filterVehicle(this.itemRepository.findAllByLocationAndDesignation(location, designation)); }
        else if (location != null && status != null && designation.isEmpty()) { return this.filterVehicle(this.forStatus(this.itemRepository.findAllByLocation(location), status)); }
        else if (location != null && status != null && !designation.isEmpty()) { return this.filterVehicle(this.forStatus(this.itemRepository.findAllByLocationAndDesignation(location, designation), status));}
        else if (location == null && status != null && designation.isEmpty()) { return this.filterVehicle(this.forStatus(this.itemRepository.findAll(), status)); }
        else { return this.filterVehicle(this.forStatus(this.itemRepository.findAllBy(designation), status));}
    }

    private List<Item> forStatus(List<Item> items, Status status) {
        List<Item> resultSet = new ArrayList<>();
        for (Item item: items) {
            if(item.getStatus() == status) {
                resultSet.add(item);
            }
        }
        return resultSet;
    }

    private void save(Item item) {
        this.itemRepository.save(item);
    }

    public void save(Item item, Condition condition){
        if (condition == null) {
            condition = Condition.FUNKTIONSFÄHIG;
        }
        item.setCondition(condition);
        this.itemRepository.save(item);
    }

    @Override
    public boolean hasLinkedItems(Item item) {
        if(item instanceof Vehicle) {
            return this.itemRepository.hasLinkedItems((Vehicle) item);
        }
        return false;
    }

    public void delete(Item item) {
        if (this.isPresent(item)) {
            this.itemRepository.delete(item);
        }
    }

    public int getVehicleCount(){ return this.findAllVehicles(null).size();}

    public int vehicleStatusCount(Status status) { return this.filterVehicle(this.forStatus(this.itemRepository.findAll(), status)).size();}

    private List<Item> filterVehicle(List<Item> items)
    {
        List<Item> vehicles = items.stream()
                .filter(i -> i instanceof Vehicle)
                .map(i -> (Vehicle) i)
                .collect(Collectors.toList());

        return vehicles;
    }

    public List<Item> findAllEquipments(String designation) {
        if(designation == null)
        {
            List<Item> equipments = this.filterEquipments(this.itemRepository.findAll());
            for (Item e: equipments) {
                System.out.println(e);
            }
            return equipments;
        }
        return this.filterEquipments(this.itemRepository.findAllBy(designation));
    }

    public List<Item> findAllEquipmentsBy(Location location, Status status, String designation) {
        if (location == null && status == null) { return this.findAllEquipments(designation); }
        else if (location != null && status == null && designation.isEmpty()) { return this.filterEquipments(this.itemRepository.findAllByLocation(location)); }
        else if (location != null && status == null && !designation.isEmpty()) { return this.filterEquipments(this.itemRepository.findAllByLocationAndDesignation(location, designation)); }
        else if (location != null && status != null && designation.isEmpty()) { return this.filterEquipments(this.forStatus(this.itemRepository.findAllByLocation(location), status)); }
        else if (location != null && status != null && !designation.isEmpty()) { return this.filterEquipments(this.forStatus(this.itemRepository.findAllByLocationAndDesignation(location, designation), status));}
        else if (location == null && status != null && designation.isEmpty()) { return this.filterEquipments(this.forStatus(this.itemRepository.findAll(), status)); }
        else { return this.filterEquipments(this.forStatus(this.itemRepository.findAllBy(designation), status));}

    }

    public void changeCondition(Item item) {
        if (item.getCondition() == Condition.FUNKTIONSFÄHIG) {
            item.setCondition(Condition.NICHT_FUNKTIONSFÄHIG);
        }
        else if (item.getCondition() == Condition.NICHT_FUNKTIONSFÄHIG) {
            item.setCondition(Condition.IN_REPARATUR);
        }
        else {item.setCondition(Condition.FUNKTIONSFÄHIG);}
        this.save(item);
    }

    public int getEquipmentCount(){ return this.findAllEquipments(null).size();}

    public int equipmentStatusCount(Status status) { return this.filterEquipments(this.forStatus(this.itemRepository.findAll(), status)).size();}

    private List<Item> filterEquipments(List<Item> items)
    {
        List<Item> equipments = items.stream()
                .filter(i -> i instanceof Equipment)
                .map(i -> (Equipment) i)
                .collect(Collectors.toList());

        return equipments;
    }

    private boolean isPresent(Item item) {
        return this.itemRepository.isPresent(item);
    }
}

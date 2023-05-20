package de.dhbw.fireinventory.application.equipment;

import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.item.ItemRepository;
import de.dhbw.fireinventory.domain.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentApplicationService {

    private final ItemRepository itemRepository;

    @Autowired
    public EquipmentApplicationService(final ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Equipment> findAllEquipments(String designation) {
        if(designation == null)
        {
            List<Equipment> equipments = this.filterEquipments(this.itemRepository.findAll());
            for (Equipment e: equipments) {
                System.out.println(e);
            }
            return equipments;
        }
        return this.filterEquipments(this.itemRepository.findAllBy(designation));
    }

    public List<Equipment> findAllEquipmentsBy(Location location, Status status, String designation) {
        if (location == null && status == null) { return this.findAllEquipments(designation); }
        else if (location != null && status == null && designation.isEmpty()) { return this.filterEquipments(this.itemRepository.findAllByLocation(location)); }
        else if (location != null && status == null && !designation.isEmpty()) { return this.filterEquipments(this.itemRepository.findAllByLocationAndDesignation(location, designation)); }
        else if (location != null && status != null && designation.isEmpty()) { return this.filterEquipments(this.forStatus(this.itemRepository.findAllByLocation(location), status)); }
        else if (location != null && status != null && !designation.isEmpty()) { return this.filterEquipments(this.forStatus(this.itemRepository.findAllByLocationAndDesignation(location, designation), status));}
        else if (location == null && status != null && designation.isEmpty()) { return this.filterEquipments(this.forStatus(this.itemRepository.findAll(), status)); }
        else { return this.filterEquipments(this.forStatus(this.itemRepository.findAllBy(designation), status));}

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

    public void saveEquipment(Equipment equipment) {
        if (equipment.getCondition() == null){
            equipment.setCondition(Condition.FUNKTIONSFÄHIG);
        }
        this.itemRepository.save(equipment);
    }

    public void changeCondition(Equipment equipment) {
        if (equipment.getCondition() == Condition.FUNKTIONSFÄHIG) {
            equipment.setCondition(Condition.NICHT_FUNKTIONSFÄHIG);
        }
        else if (equipment.getCondition() == Condition.NICHT_FUNKTIONSFÄHIG) {
            equipment.setCondition(Condition.IN_REPARATUR);
        }
        else {equipment.setCondition(Condition.FUNKTIONSFÄHIG);}
        this.saveEquipment(equipment);
    }

    public void deleteEquipment(Equipment equipment)
    {
        this.itemRepository.delete(equipment);
    }

    public int getEquipmentCount(){ return this.findAllEquipments(null).size();}

    public int equipmentStatusCount(Status status) { return this.filterEquipments(this.forStatus(this.itemRepository.findAll(), status)).size();}

    private List<Equipment> filterEquipments(List<Item> items)
    {
        List<Equipment> equipments = items.stream()
                .filter(i -> i instanceof Equipment)
                .map(i -> (Equipment) i)
                .collect(Collectors.toList());

        return equipments;
    }
}

package de.dhbw.fireinventory.application.equipment;

import de.dhbw.fireinventory.domain.Condition;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.item.ItemRepository;
import de.dhbw.fireinventory.domain.location.InternalPlace;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.VehiclePlace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return this.filterEquipments(this.itemRepository.findAllBy(designation));
    }

    public List<Equipment> findAllEquipmentsBy(Location location, Status status, String designation) {
        if (location == null && status == null) { return this.findAllEquipments(designation); }
        else if (location != null && status == null && designation.isEmpty()) { return this.filterEquipments(this.itemRepository.findAllByLocation(location)); }
        else if (location != null && status == null && !designation.isEmpty()) { return this.filterEquipments(this.itemRepository.findAllByLocationAndDesignation(location, designation)); }
        else if (location != null && status != null && designation.isEmpty()) { return this.filterEquipments(this.itemRepository.findAllByLocationAndStatus(location, status)); }
        else if (location != null && status != null && !designation.isEmpty()) { return this.filterEquipments(this.itemRepository.findAllByLocationStatusAndDesignation(location, status, designation));}
        else if (location == null && status != null && designation.isEmpty()) { return this.filterEquipments(this.itemRepository.findAllByStatus(status)); }
        else { return this.filterEquipments(this.itemRepository.findAllByStatusAndDesignation(status, designation));}

    }

    public void saveEquipment(Equipment equipment) {
        this.itemRepository.save(equipment);
    }

    public void deleteEquipment(Equipment equipment)
    {
        this.itemRepository.delete(equipment);
    }

    public int getEquipmentCount(){ return this.findAllEquipments(null).size();}

    public int equipmentStatusCount(Status status) { return this.filterEquipments(this.itemRepository.findAllByStatus(status)).size();}

    public void saveEquipment(Equipment equipment, Condition condition) {
        Location location = equipment.getLocation();
        if(condition == Condition.FUNKTIONSFÄHIG)
        {
            if(location instanceof VehiclePlace)
            {
                VehiclePlace vehiclePlace = (VehiclePlace) location;
                Status vehicleStatus = vehiclePlace.getVehicle().getStatus();
                Location vehicleLocation = vehiclePlace.getVehicle().getLocation();
                if(vehicleStatus == Status.EINSATZBEREIT){
                    equipment.setStatus(Status.EINSATZBEREIT);
                }
                else if(vehicleLocation instanceof InternalPlace){
                    equipment.setStatus(Status.VOR_ORT);
                }
                else {equipment.setStatus(Status.NICHT_VOR_ORT);}
            }
            else if(location instanceof InternalPlace) {equipment.setStatus(Status.VOR_ORT);}
            else {equipment.setStatus(Status.NICHT_VOR_ORT);}
        }
        else if(condition == Condition.NICHT_FUNKTIONSFÄHIG) {equipment.setStatus(Status.KAPUTT);}
        else {equipment.setStatus(Status.IN_REPARATUR);}
        this.saveEquipment(equipment);
    }

    private List<Equipment> filterEquipments(List<Item> items)
    {
        List<Equipment> equipments = items.stream()
                .filter(i -> i instanceof Equipment)
                .map(i -> (Equipment) i)
                .collect(Collectors.toList());

        return equipments;
    }
}

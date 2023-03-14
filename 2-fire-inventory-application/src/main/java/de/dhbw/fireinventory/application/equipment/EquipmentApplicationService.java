package de.dhbw.fireinventory.application.equipment;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.equipment.EquipmentRepository;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentApplicationService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentApplicationService(final EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public List<Equipment> findAllEquipments(String designation) {
        return this.equipmentRepository.findAllBy(designation);
    }

    public List<Equipment> findAllEquipmentsBy(Location location, Status status, String designation) {
        if (location == null && status == null) { return this.findAllEquipments(designation); }
        else if (location != null && status == null && designation.isEmpty()) { return this.equipmentRepository.findAllByLocation(location); }
        else if (location != null && status == null && !designation.isEmpty()) { return this.equipmentRepository.findAllByLocationAndDesignation(location, designation); }
        else if (location != null && status != null && designation.isEmpty()) { return this.equipmentRepository.findAllByLocationAndStatus(location, status); }
        else if (location != null && status != null && !designation.isEmpty()) { return this.equipmentRepository.findAllByLocationStatusAndDesignation(location, status, designation);}
        else if (location == null && status != null && designation.isEmpty()) { return this.equipmentRepository.findAllByStatus(status); }
        else { return this.equipmentRepository.findAllByStatusAndDesignation(status, designation);}

    }

    public void saveEquipment(Equipment equipment) {
        this.equipmentRepository.save(equipment);
    }

    public void deleteEquipment(Equipment equipment)
    {
        this.equipmentRepository.delete(equipment);
    }

    public int getEquipmentCount(){ return this.findAllEquipments(null).size();}

    public int equipmentStatusCount(String status) { return this.equipmentRepository.equipmentStatusCount(status);}
}

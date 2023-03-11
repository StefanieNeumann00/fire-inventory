package de.dhbw.fireinventory.application.equipment;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.equipment.EquipmentRepository;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;
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

    public List<Equipment> findAllEquipments(String filterText) {
        return this.equipmentRepository.findAllBy(filterText);
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

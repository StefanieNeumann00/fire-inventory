package de.dhbw.fireinventory.application.equipment;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.equipment.EquipmentRepository;
import de.dhbw.fireinventory.domain.location.Location;
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

    public List<Equipment> findAllEquipments() {
        return this.equipmentRepository.findAllEquipments();
    }

    public void saveEquipment(Equipment equipment) {
        this.equipmentRepository.save(equipment);
    }
}

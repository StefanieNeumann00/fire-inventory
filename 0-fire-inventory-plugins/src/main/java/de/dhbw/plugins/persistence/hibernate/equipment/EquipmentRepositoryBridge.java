package de.dhbw.plugins.persistence.hibernate.equipment;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.equipment.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EquipmentRepositoryBridge implements EquipmentRepository {

    private final SpringDataEquipmentRepository springDataEquipmentRepository;

    @Autowired
    public EquipmentRepositoryBridge(final SpringDataEquipmentRepository springDataEquipmentRepository) {
        this.springDataEquipmentRepository = springDataEquipmentRepository;
    }

    @Override
    public List<Equipment> findAllEquipments() {
        return this.springDataEquipmentRepository.findAll();
    }

    @Override
    public List<Equipment> findEquipmentWithDesignation(final String designation) {
        return this.springDataEquipmentRepository.findEquipmentByDesignation(designation);
    }

    @Override
    public Equipment save(final Equipment equipment) {
        return this.springDataEquipmentRepository.save(equipment);
    }
}

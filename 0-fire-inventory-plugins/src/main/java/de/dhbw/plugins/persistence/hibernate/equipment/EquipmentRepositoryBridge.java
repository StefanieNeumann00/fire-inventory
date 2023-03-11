package de.dhbw.plugins.persistence.hibernate.equipment;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.equipment.EquipmentRepository;
import de.dhbw.fireinventory.domain.status.Status;
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
    public List<Equipment> findAllBy(final String filterText) {
        if(filterText == null)
        {
            return this.springDataEquipmentRepository.findAll();
        }
        return this.springDataEquipmentRepository.findAllBy(filterText);
    }

    @Override
    public Equipment save(final Equipment equipment) {
        return this.springDataEquipmentRepository.save(equipment);
    }

    @Override
    public void delete(final Equipment equipment){
        this.springDataEquipmentRepository.delete(equipment);
    }

    public int equipmentStatusCount(String status) { return this.springDataEquipmentRepository.equipmentStatusCount(status);}
}

package de.dhbw.plugins.persistence.hibernate.equipment;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.equipment.EquipmentRepository;
import de.dhbw.fireinventory.domain.location.Location;
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

    @Override
    public List<Equipment> findAllByLocation(Location location) {
        return this.springDataEquipmentRepository.findAllByLocation(location);
    }

    @Override
    public List<Equipment> findAllByLocationAndDesignation(Location location, String designation) {
        return this.springDataEquipmentRepository.findAllByLocationAndDesignation(location, designation);
    }

    @Override
    public List<Equipment> findAllByLocationAndStatus(Location location, Status status) {
        return this.springDataEquipmentRepository.findAllByLocationAndStatus(location, status);
    }

    @Override
    public List<Equipment> findAllByLocationStatusAndDesignation(Location location, Status status, String designation) {
        return this.springDataEquipmentRepository.findAllByLocationStatusAndDesignation(location, status, designation);
    }

    @Override
    public List<Equipment> findAllByStatus(Status status) {
        return this.springDataEquipmentRepository.findAllByStatus(status);
    }

    @Override
    public List<Equipment> findAllByStatusAndDesignation(Status status, String designation) {
        return this.springDataEquipmentRepository.findAllByStatusAndDesignation(status, designation);
    }
}

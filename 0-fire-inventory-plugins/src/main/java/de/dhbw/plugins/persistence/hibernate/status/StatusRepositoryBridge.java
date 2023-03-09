package de.dhbw.plugins.persistence.hibernate.status;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.status.StatusRepository;
import de.dhbw.plugins.persistence.hibernate.equipment.SpringDataEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.dhbw.fireinventory.domain.status.StatusRepository;

import java.util.List;

@Repository
public class StatusRepositoryBridge implements StatusRepository {

    private final SpringDataStatusRepository springDataStatusRepository;

    @Autowired
    public StatusRepositoryBridge(final SpringDataStatusRepository springDataStatusRepository) {
        this.springDataStatusRepository = springDataStatusRepository;
    }

    @Override
    public List<Status> findAllStatuses() {
        return this.springDataStatusRepository.findAll();
    }


    @Override
    public Status save(final Status status) {
        return this.springDataStatusRepository.save(status);
    }
}
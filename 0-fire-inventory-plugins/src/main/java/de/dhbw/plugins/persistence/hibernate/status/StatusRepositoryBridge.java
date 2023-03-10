package de.dhbw.plugins.persistence.hibernate.status;

import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.status.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
    public Status getStatusByDesignation(String designation) {
        return this.springDataStatusRepository.getStatusByDesignation(designation);
    }

    @Override
    public Status save(final Status status) {
        return this.springDataStatusRepository.save(status);
    }
}

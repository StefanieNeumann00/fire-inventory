package de.dhbw.fireinventory.application.status;

import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.LocationRepository;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.status.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusApplicationService {

    private final StatusRepository statusRepository;
    @Autowired
    public StatusApplicationService(final StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<Status> findAllStatuses() {
        return this.statusRepository.findAllStatuses();
    }

    public void saveStatus(Status status){ this.statusRepository.save(status); }
}

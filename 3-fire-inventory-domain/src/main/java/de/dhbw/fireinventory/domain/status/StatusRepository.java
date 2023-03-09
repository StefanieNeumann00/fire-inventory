package de.dhbw.fireinventory.domain.status;

import de.dhbw.fireinventory.domain.location.Location;

import java.util.List;

public interface StatusRepository {

    List<Status> findAllStatuses();

    Status save(Status status);
}

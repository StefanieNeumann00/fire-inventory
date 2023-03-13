package de.dhbw.fireinventory.domain.equipment;

import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import java.util.List;

public interface EquipmentRepository {

    List<Equipment> findAllBy(String filterText);

    Equipment save(Equipment equipment);

    void delete(Equipment equipment);

    int equipmentStatusCount(String status);

    List<Equipment> findAllByLocation(Location location);

    List<Equipment> findAllByLocationAndDesignation(Location location, String designation);

    List<Equipment> findAllByLocationAndStatus(Location location, Status status);

    List<Equipment> findAllByLocationStatusAndDesignation(Location location, Status status, String designation);

    List<Equipment> findAllByStatus(Status status);

    List<Equipment> findAllByStatusAndDesignation(Status status, String designation);
}

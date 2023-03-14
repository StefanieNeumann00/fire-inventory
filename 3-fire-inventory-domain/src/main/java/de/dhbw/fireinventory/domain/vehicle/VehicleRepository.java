package de.dhbw.fireinventory.domain.vehicle;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.status.Status;

import java.util.List;

public interface VehicleRepository {

    List<Vehicle> findAllBy(String filterText);

    Vehicle save(Vehicle vehicle);

    void delete(Vehicle vehicle);

    int vehicleStatusCount(String status);

    List<Vehicle> findAllByPlace(Place place);

    List<Vehicle> findAllByPlaceAndDesignation(Place place, String designation);

    List<Vehicle> findAllByPlaceAndStatus(Place place, Status status);

    List<Vehicle> findAllByPlaceStatusAndDesignation(Place place, Status status, String designation);

    List<Vehicle> findAllByStatus(Status status);

    List<Vehicle> findAllByStatusAndDesignation(Status status, String designation);
}

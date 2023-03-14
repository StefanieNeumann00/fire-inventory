package de.dhbw.plugins.persistence.hibernate.vehicle;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import de.dhbw.fireinventory.domain.vehicle.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VehicleRepositoryBridge implements VehicleRepository {

    private final SpringDataVehicleRepository springDataVehicleRepository;

    @Autowired
    public VehicleRepositoryBridge(final SpringDataVehicleRepository springDataVehicleRepository) {
        this.springDataVehicleRepository = springDataVehicleRepository;
    }

    @Override
    public List<Vehicle> findAllBy(String designation) {
        if(designation == null)
        {
            return this.springDataVehicleRepository.findAll();
        }
        return this.springDataVehicleRepository.findAllBy(designation);

    }

    @Override
    public Vehicle save(final Vehicle vehicle) {
        return this.springDataVehicleRepository.save(vehicle);
    }

    @Override
    public int vehicleStatusCount(final String status) { return this.springDataVehicleRepository.vehicleStatusCount(status);}

    @Override
    public List<Vehicle> findAllByPlace(Place place) {
        return this.springDataVehicleRepository.findAllByPlace(place);
    }

    @Override
    public List<Vehicle> findAllByPlaceAndDesignation(Place place, String designation) {
        return this.springDataVehicleRepository.findAllByPlaceAndDesignation(place, designation);
    }

    @Override
    public List<Vehicle> findAllByPlaceAndStatus(Place place, Status status) {
        return this.springDataVehicleRepository.findAllByPlaceAndStatus(place, status);
    }

    @Override
    public List<Vehicle> findAllByPlaceStatusAndDesignation(Place place, Status status, String designation) {
        return this.springDataVehicleRepository.findAllByPlaceStatusAndDesignation(place, status, designation);
    }

    @Override
    public List<Vehicle> findAllByStatus(Status status) {
        return this.springDataVehicleRepository.findAllByStatus(status);
    }

    @Override
    public List<Vehicle> findAllByStatusAndDesignation(Status status, String designation) {
        return this.springDataVehicleRepository.findAllByStatusAndDesignation(status, designation);
    }

    @Override
    public void delete(Vehicle vehicle) { this.springDataVehicleRepository.delete(vehicle);}
}

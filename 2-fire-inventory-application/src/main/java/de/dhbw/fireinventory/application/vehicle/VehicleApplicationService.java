package de.dhbw.fireinventory.application.vehicle;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.LocationRepository;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import de.dhbw.fireinventory.domain.vehicle.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleApplicationService {

    private LocationRepository locationRepository;
    private final VehicleRepository vehicleRepository;
    @Autowired
    public VehicleApplicationService(final VehicleRepository vehicleRepository, LocationRepository locationRepository) {
        this.vehicleRepository = vehicleRepository;
        this.locationRepository = locationRepository;
    }

    public List<Vehicle> findAllVehicles(String filterText) {
        return this.vehicleRepository.findAllBy(filterText);
    }

    public List<Vehicle> findAllVehiclesBy(Place place, Status status, String designation) {
        if (place == null && status == null) { return this.findAllVehicles(designation); }
        else if (place != null && status == null && designation.isEmpty()) { return this.vehicleRepository.findAllByPlace(place); }
        else if (place != null && status == null && !designation.isEmpty()) { return this.vehicleRepository.findAllByPlaceAndDesignation(place, designation); }
        else if (place != null && status != null && designation.isEmpty()) { return this.vehicleRepository.findAllByPlaceAndStatus(place, status); }
        else if (place != null && status != null && !designation.isEmpty()) { return this.vehicleRepository.findAllByPlaceStatusAndDesignation(place, status, designation);}
        else if (place == null && status != null && designation.isEmpty()) { return this.vehicleRepository.findAllByStatus(status); }
        else { return this.vehicleRepository.findAllByStatusAndDesignation(status, designation);}

    }

    public void saveVehicle(Vehicle vehicle){
        this.vehicleRepository.save(vehicle);
        Location location = new Location();
        location.setVehicle(vehicle);
        this.locationRepository.save(location);
    }

    public void deleteVehicle(Vehicle vehicle) { this.vehicleRepository.delete(vehicle); }

    public int getVehicleCount(){ return this.findAllVehicles(null).size();}

    public int vehicleStatusCount(String status) { return this.vehicleRepository.vehicleStatusCount(status);}
}

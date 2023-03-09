package de.dhbw.fireinventory.application.vehicle;

import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.LocationRepository;
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

    public List<Vehicle> findAllVehicles() {
        return this.vehicleRepository.findAllVehicles();
    }

    public void saveVehicle(Vehicle vehicle){
        this.vehicleRepository.save(vehicle);
        Location location = new Location();
        location.setVehicle(vehicle);
        this.locationRepository.save(location);
    }
}

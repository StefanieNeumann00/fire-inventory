package de.dhbw.plugins.persistence.hibernate.vehicle;

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
    public List<Vehicle> findAllVehicles() {
        return this.springDataVehicleRepository.findAll();
    }

    @Override
    public Vehicle save(final Vehicle vehicle) {
        return this.springDataVehicleRepository.save(vehicle);
    }

    public int vehicleStatusCount(final String status) { return this.springDataVehicleRepository.vehicleStatusCount(status);}
}

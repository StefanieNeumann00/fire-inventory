package de.dhbw.plugins.persistence.hibernate.vehicle;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataVehicleRepository extends JpaRepository<Vehicle, UUID> {
}

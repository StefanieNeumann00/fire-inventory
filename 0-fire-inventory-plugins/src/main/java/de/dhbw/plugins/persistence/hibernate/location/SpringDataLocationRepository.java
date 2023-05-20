package de.dhbw.plugins.persistence.hibernate.location;

import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.VehiclePlace;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataLocationRepository extends JpaRepository<Location, UUID> {

    @Query("select l from Location l where lower(l.designation) like lower(concat('%', :designation, '%'))")
    List<Location> findAllBy(@Param("designation") String designation);

    @Query("select l from Location l where l.vehicle = :vehicle")
    VehiclePlace findLocationForVehicle(@Param("vehicle")Vehicle vehicle);
}

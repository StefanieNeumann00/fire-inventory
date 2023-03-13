package de.dhbw.plugins.persistence.hibernate.location;

import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SpringDataLocationRepository extends JpaRepository<Location, UUID> {

    @Query("select l from Location l where l.vehicle = :vehicle ")
    Location getLocationForVehicle(@Param("vehicle") Vehicle vehicle);

    @Query("select l from Location l where l.place = :place")
    Location getLocationForPlace(@Param("place") Place place);
}

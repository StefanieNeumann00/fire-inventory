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

    @Query(value = "select case when exists (select * from Item where location = :location) then cast (1 as bit) else cast (0 as bit) end", nativeQuery = true)
    boolean hasLinkedItems(@Param("location")Location location);

    @Query(value = "select case when exists (select * from Location where id = :locationId) then cast (1 as bit) else cast (0 as bit) end", nativeQuery = true)
    boolean isPresent(@Param("locationId") UUID locationId);
}

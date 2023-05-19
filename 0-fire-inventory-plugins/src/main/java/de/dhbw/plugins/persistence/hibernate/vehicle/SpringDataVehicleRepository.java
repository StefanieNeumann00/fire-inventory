package de.dhbw.plugins.persistence.hibernate.vehicle;

import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataVehicleRepository extends JpaRepository<Vehicle, UUID> {

    @Query("select v from Vehicle v where lower(v.designation) like lower(concat('%', :designation, '%'))")
    List<Vehicle> findAllBy(@Param("designation") String designation);

    @Query("select count(v) from Vehicle v where v.status = :status")
    int vehicleStatusCount(@Param("status") Status status);

    @Query("select v from Vehicle v where v.location = :location")
    List<Vehicle> findAllByLocation(@Param("location") Location location);

    @Query("select v from Vehicle v where v.location = :location and lower(v.designation) like lower(concat('%', :designation, '%'))")
    List<Vehicle> findAllByLocationAndDesignation(@Param("location") Location location, @Param("designation") String designation);

    @Query("select v from Vehicle v where v.location = :location and v.status = :status")
    List<Vehicle> findAllByLocationAndStatus(@Param("location") Location location, @Param("status") Status status);

    @Query("select v from Vehicle v where v.location = :location and v.status = :status and lower(v.designation) like lower(concat('%', :designation, '%'))")
    List<Vehicle> findAllByLocationStatusAndDesignation(@Param("location") Location location, @Param("status") Status status, @Param("designation") String designation);

    @Query("select v from Vehicle v where v.status = :status")
    List<Vehicle> findAllByStatus(@Param("status") Status status);

    @Query("select v from Vehicle v where v.status = :status and lower(v.designation) like lower(concat('%', :designation, '%'))")
    List<Vehicle> findAllByStatusAndDesignation(@Param("status") Status status, @Param("designation") String designation);
}

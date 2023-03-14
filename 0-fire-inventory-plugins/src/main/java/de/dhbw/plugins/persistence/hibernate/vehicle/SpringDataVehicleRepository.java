package de.dhbw.plugins.persistence.hibernate.vehicle;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataVehicleRepository extends JpaRepository<Vehicle, UUID> {

    @Query("select v from Vehicle v where lower(v.designation) like lower(concat('%', :designation, '%'))")
    List<Vehicle> findAllBy(@Param("designation") String designation);

    @Query("select count(v) from Vehicle v where v.status = (select s from Status s where s.designation = :statusDesignation)")
    int vehicleStatusCount(@Param("statusDesignation") String statusDesignation);

    @Query("select v from Vehicle v where v.place = :place")
    List<Vehicle> findAllByPlace(@Param("place") Place place);

    @Query("select v from Vehicle v where v.place = :place and lower(v.designation) like lower(concat('%', :designation, '%'))")
    List<Vehicle> findAllByPlaceAndDesignation(@Param("place") Place place, @Param("designation") String designation);

    @Query("select v from Vehicle v where v.place = :place and v.status = :status")
    List<Vehicle> findAllByPlaceAndStatus(@Param("place") Place place, @Param("status") Status status);

    @Query("select v from Vehicle v where v.place = :place and v.status = :status and lower(v.designation) like lower(concat('%', :designation, '%'))")
    List<Vehicle> findAllByPlaceStatusAndDesignation(@Param("place") Place place, @Param("status") Status status, @Param("designation") String designation);

    @Query("select v from Vehicle v where v.status = :status")
    List<Vehicle> findAllByStatus(@Param("status") Status status);

    @Query("select v from Vehicle v where v.status = :status and lower(v.designation) like lower(concat('%', :designation, '%'))")
    List<Vehicle> findAllByStatusAndDesignation(@Param("status") Status status, @Param("designation") String designation);
}

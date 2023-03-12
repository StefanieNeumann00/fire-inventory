package de.dhbw.plugins.persistence.hibernate.vehicle;

import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SpringDataVehicleRepository extends JpaRepository<Vehicle, UUID> {

    @Query("select count(v) from Vehicle v where v.status = (select s from Status s where s.designation = :statusDesignation)")
    int vehicleStatusCount(@Param("statusDesignation") String statusDesignation);
}

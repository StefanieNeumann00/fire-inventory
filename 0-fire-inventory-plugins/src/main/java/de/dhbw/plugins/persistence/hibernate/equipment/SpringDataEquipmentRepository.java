package de.dhbw.plugins.persistence.hibernate.equipment;

import java.util.List;
import java.util.UUID;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.hibernate.annotations.Formula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataEquipmentRepository extends JpaRepository<Equipment, UUID> {

    @Query("select e from Equipment e where lower(e.designation) like lower(concat('%', :filterText, '%'))")
    List<Equipment> findAllBy(@Param("filterText") String filterText);

    @Query("select count(e) from Equipment e where e.status = (select s from Status s where s.designation = :statusDesignation)")
    int equipmentStatusCount(@Param("statusDesignation") String statusDesignation);

    @Query("select e from Equipment e where e.location = :location")
    List<Equipment> findAllByLocation(@Param("location") Location location);

    @Query("select e from Equipment e where e.location = :location and lower(e.designation) like lower(concat('%', :designation, '%'))")
    List<Equipment> findAllByLocationAndDesignation(@Param("location") Location location, @Param("designation") String designation);

    @Query("select e from Equipment e where e.location = :location and e.status = :status")
    List<Equipment> findAllByLocationAndStatus(@Param("location") Location location, @Param("status") Status status);

    @Query("select e from Equipment e where e.location = :location and e.status = :status and lower(e.designation) like lower(concat('%', :designation, '%'))")
    List<Equipment> findAllByLocationStatusAndDesignation(@Param("location") Location location, @Param("status") Status status, @Param("designation") String designation);

    @Query("select e from Equipment e where e.status = :status")
    List<Equipment> findAllByStatus(@Param("status") Status status);

    @Query("select e from Equipment e where e.status = :status and lower(e.designation) like lower(concat('%', :designation, '%'))")
    List<Equipment> findAllByStatusAndDesignation(@Param("status") Status status, @Param("designation") String designation);

}

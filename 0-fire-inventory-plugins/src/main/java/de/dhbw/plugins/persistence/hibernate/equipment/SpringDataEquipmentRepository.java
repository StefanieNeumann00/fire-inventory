package de.dhbw.plugins.persistence.hibernate.equipment;

import java.util.List;
import java.util.UUID;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.status.Status;
import org.hibernate.annotations.Formula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataEquipmentRepository extends JpaRepository<Equipment, UUID> {

    @Query("select e from Equipment e where lower(e.designation) like lower(concat('%', :filterText, '%'))")
    List<Equipment> findAllBy(@Param("filterText") String filterText);

    @Query("select count(e) from Equipment e where e.status = (select s from Status s where s.designation = :statusDesignation)")
    int equipmentStatusCount(@Param("statusDesignation") String statusDesignation);

}

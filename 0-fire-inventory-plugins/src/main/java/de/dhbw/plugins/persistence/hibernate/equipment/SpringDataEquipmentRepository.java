package de.dhbw.plugins.persistence.hibernate.equipment;

import java.util.List;
import java.util.UUID;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataEquipmentRepository extends JpaRepository<Equipment, UUID> {

    @Query("select e from Equipment e where lower(e.designation) like lower(concat('%', :filterText, '%'))")
    List<Equipment> findAllBy(@Param("filterText") String filterText);

}

package de.dhbw.plugins.persistence.hibernate.equipment;

import java.util.List;
import java.util.UUID;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataEquipmentRepository extends JpaRepository<Equipment, UUID> {

    List<Equipment> findEquipmentByDesignation(final String designation);

    @Query("select e from Equipment e where lower(e.designation) like lower(concat('%', :filterText, '%'))")
    List<Equipment> findAllBy(String filterText);
}

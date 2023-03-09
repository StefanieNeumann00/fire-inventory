package de.dhbw.plugins.persistence.hibernate.equipment;

import java.util.List;
import java.util.UUID;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataEquipmentRepository extends JpaRepository<Equipment, UUID> {

    List<Equipment> findEquipmentByDesignation(final String designation);

}

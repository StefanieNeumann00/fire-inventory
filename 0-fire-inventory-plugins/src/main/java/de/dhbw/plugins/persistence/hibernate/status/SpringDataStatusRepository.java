package de.dhbw.plugins.persistence.hibernate.status;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.status.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataStatusRepository extends JpaRepository<Status, UUID> {
}

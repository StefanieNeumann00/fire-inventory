package de.dhbw.plugins.persistence.hibernate.status;

import de.dhbw.fireinventory.domain.status.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface SpringDataStatusRepository extends JpaRepository<Status, UUID> {

    @Query("select s from Status s where s.designation = :designation")
    public Status getStatusByDesignation(String designation);
}

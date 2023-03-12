package de.dhbw.plugins.persistence.hibernate.appointment;

import de.dhbw.fireinventory.domain.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SpringDataAppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query(value = "select * from Appointment order by dueDate", nativeQuery = true)
    public List<Appointment> findAllSortedByDate();
}

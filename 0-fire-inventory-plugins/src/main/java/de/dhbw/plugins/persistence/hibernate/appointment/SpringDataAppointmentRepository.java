package de.dhbw.plugins.persistence.hibernate.appointment;

import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataAppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query(value = "select a from Appointment a order by a.dueDate")
    public List<Appointment> findAllSortedByDate();

    @Query(value = "select a from Appointment a where a.item = :item")
    public List<Appointment> findForItem(@Param("item") Item item);

    @Query(value = "select a from Appointment a where a.item = :item and lower(a.designation) like lower(concat('%', :designation, '%'))")
    public List<Appointment> findForItemAndDes(@Param("item") Item item, @Param("designation") String designation);

    @Query(value = "select a from Appointment a where lower(a.designation) like lower(concat('%', :designation, '%'))")
    public List<Appointment> findForDesignation(@Param("designation") String designation);
}

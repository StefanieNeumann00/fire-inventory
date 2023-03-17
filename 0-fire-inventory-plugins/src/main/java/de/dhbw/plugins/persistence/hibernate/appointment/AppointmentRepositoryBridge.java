package de.dhbw.plugins.persistence.hibernate.appointment;

import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.appointment.AppointmentRepository;
import de.dhbw.fireinventory.domain.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppointmentRepositoryBridge implements AppointmentRepository {

    private final SpringDataAppointmentRepository springDataAppointmentRepository;

    @Autowired
    public AppointmentRepositoryBridge(final SpringDataAppointmentRepository springDataAppointmentRepository) {
        this.springDataAppointmentRepository = springDataAppointmentRepository;
    }

    @Override
    public List<Appointment> findAllSortedByDate() {
        return this.springDataAppointmentRepository.findAllSortedByDate();
    }

    @Override
    public Appointment save(Appointment appointment) {
        return this.springDataAppointmentRepository.save(appointment);
    }

    @Override
    public void delete(Appointment appointment) {
        this.springDataAppointmentRepository.delete(appointment);
    }

    @Override
    public List<Appointment> findAppointmentsForItem(Item item) {
        return this.springDataAppointmentRepository.findForItem(item);
    }

    @Override
    public List<Appointment> findAppointmentsForDesignation(String designation) {
        return this.springDataAppointmentRepository.findForDesignation(designation);
    }

    @Override
    public List<Appointment> findAppointmentsForItemAndDes(Item item, String designation) {
        return this.springDataAppointmentRepository.findForItemAndDes(item, designation);
    }
}

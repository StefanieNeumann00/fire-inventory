package de.dhbw.fireinventory.application.appointment;

import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.appointment.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentApplicationService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentApplicationService(final AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> findAllSortedByDate() {
        return this.appointmentRepository.findAllSortedByDate();
    }

    public void saveAppointment(Appointment appointment) {
        this.appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Appointment appointment)
    {
        this.appointmentRepository.delete(appointment);
    }

    public List<Appointment> findAllAppointmentsFor(Item item, String designation) {
        if (item == null && designation.isEmpty()) {
            return this.findAllSortedByDate();
        } else if (item == null && !designation.isEmpty()) {
            return this.appointmentRepository.findAppointmentsForDesignation(designation);
        } else if (item != null && designation.isEmpty()) {
            return this.appointmentRepository.findAppointmentsForItem(item);
        } else {
            return this.appointmentRepository.findAppointmentsForItemAndDes(item, designation);
        }
    }

    public List<LocalDate> findAppointmentDatesFor(Item item, String designation) {
        List<Appointment> appointments = this.findAllAppointmentsFor(item, designation);
        List<LocalDate> dates = new ArrayList<>();
        for (Appointment appointment: appointments) {
            dates.add(appointment.getDueDate());
        }
        return dates;
    }
}

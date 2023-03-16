package de.dhbw.fireinventory.application.appointment;

import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.appointment.AppointmentRepository;
import de.dhbw.fireinventory.domain.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
            return this.appointmentRepository.findAppointmentsFor(designation);
        } else if (item != null && designation.isEmpty()) {
            return this.appointmentRepository.findAppointmentsFor(item);
        } else {
            return this.appointmentRepository.findAppointmentsFor(item, designation);
        }
    }

    public List<Date> findAppointmentDatesFor(Item item, String designation) {
        List<Appointment> appointments = this.findAllAppointmentsFor(item, designation);
        List<Date> dates = new ArrayList<>();
        for (Appointment appointment: appointments) {
            dates.add(appointment.getDueDate());
        }
        return dates;
    }
}

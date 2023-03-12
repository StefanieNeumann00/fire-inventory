package de.dhbw.fireinventory.application.appointment;

import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.appointment.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

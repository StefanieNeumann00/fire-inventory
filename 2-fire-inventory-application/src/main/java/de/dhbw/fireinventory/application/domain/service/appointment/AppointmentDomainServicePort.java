package de.dhbw.fireinventory.application.domain.service.appointment;

import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.item.Item;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentDomainServicePort {

    List<Appointment> findAllSortedByDate();

    void saveAppointment(Appointment appointment);

    void deleteAppointment(Appointment appointment);

    List<Appointment> findAllAppointmentsFor(Item item, String designation);

    List<LocalDate> findAppointmentDatesFor(Item item, String designation);
}

package de.dhbw.fireinventory.domain.appointment;

import de.dhbw.fireinventory.domain.item.Item;

import java.util.List;

public interface AppointmentRepository {

    List<Appointment> findAllSortedByDate();

    Appointment save(Appointment appointment);

    void delete(Appointment appointment);

    List<Appointment> findAppointmentsFor(Item item);

    List<Appointment> findAppointmentsFor(String designation);

    List<Appointment> findAppointmentsFor(Item item, String designation);
}

package de.dhbw.fireinventory.domain.appointment;

import java.util.List;

public interface AppointmentRepository {

    List<Appointment> findAllSortedByDate();

    Appointment save(Appointment appointment);

    void delete(Appointment appointment);
}

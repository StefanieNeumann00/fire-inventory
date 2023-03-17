package de.dhbw.fireinventory.domain.appointment;

import de.dhbw.fireinventory.domain.item.Item;

import java.util.List;

public interface AppointmentRepository {

    List<Appointment> findAllSortedByDate();

    Appointment save(Appointment appointment);

    void delete(Appointment appointment);

    List<Appointment> findAppointmentsForItem(Item item);

    List<Appointment> findAppointmentsForDesignation(String designation);

    List<Appointment> findAppointmentsForItemAndDes(Item item, String designation);
}

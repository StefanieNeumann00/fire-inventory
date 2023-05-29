package de.dhbw.fireinventory.adapter.application.appointment;

import de.dhbw.fireinventory.application.domain.service.appointment.AppointmentResource;
import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.fireinventory.domain.item.Item;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentApplicationAdapter {

    List<AppointmentResource> findAllSortedByDate();

    void saveAppointment(AppointmentResource appointment);

    void deleteAppointment(AppointmentResource appointment);

    List<AppointmentResource> findAllAppointmentsFor(ItemResource itemResource, String designation);

    List<LocalDate> findAppointmentDatesFor(ItemResource itemResource, String designation);
}

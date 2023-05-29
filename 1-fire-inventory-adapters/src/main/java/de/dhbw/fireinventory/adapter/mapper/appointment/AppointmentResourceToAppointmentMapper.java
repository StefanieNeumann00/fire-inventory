package de.dhbw.fireinventory.adapter.mapper.appointment;

import de.dhbw.fireinventory.adapter.mapper.item.ItemResourceToItemMapper;
import de.dhbw.fireinventory.application.domain.service.appointment.AppointmentResource;
import de.dhbw.fireinventory.domain.appointment.Appointment;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AppointmentResourceToAppointmentMapper implements Function<AppointmentResource, Appointment> {

    ItemResourceToItemMapper itemResourceToItemMapper = new ItemResourceToItemMapper();
    @Override
    public Appointment apply(final AppointmentResource appointmentResource) {
        return map(appointmentResource);
    }

    private Appointment map(final AppointmentResource appointmentResource) {
        Appointment appointment = new Appointment();
        appointment.setId(appointmentResource.getId());
        appointment.setDesignation(appointmentResource.getDesignation());
        appointment.setItem(itemResourceToItemMapper.apply(appointmentResource.getItemResource()));
        appointment.setDueDate(appointmentResource.getDueDate());

        return appointment;
    }
}

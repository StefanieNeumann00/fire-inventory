package de.dhbw.fireinventory.adapter.mapper.appointment;

import de.dhbw.fireinventory.adapter.mapper.item.ItemToItemResourceMapper;
import de.dhbw.fireinventory.application.domain.service.appointment.AppointmentResource;
import de.dhbw.fireinventory.domain.appointment.Appointment;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AppointmentToAppointmentResourceMapper implements Function<Appointment, AppointmentResource> {

    ItemToItemResourceMapper itemToItemResourceMapper = new ItemToItemResourceMapper();

    @Override
    public AppointmentResource apply(final Appointment appointment) {
        return map(appointment);
    }

    private AppointmentResource map(final Appointment appointment) {
        return new AppointmentResource(appointment.getId(), appointment.getDesignation(), itemToItemResourceMapper.apply(appointment.getItem()), appointment.getDueDate());
    }

}

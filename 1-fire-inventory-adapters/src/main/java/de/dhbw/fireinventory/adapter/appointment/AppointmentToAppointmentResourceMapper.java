package de.dhbw.fireinventory.adapter.appointment;

import de.dhbw.fireinventory.domain.appointment.Appointment;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AppointmentToAppointmentResourceMapper implements Function<Appointment, AppointmentResource> {

    @Override
    public AppointmentResource apply(final Appointment appointment) {
        return map(appointment);
    }

    private AppointmentResource map(final Appointment appointment) {
        return new AppointmentResource(appointment.getDesignation(), appointment.getVehicle(), appointment.getEquipment(), appointment.getDueDate(), appointment.getEndDate(), appointment.getInterval());
    }

}

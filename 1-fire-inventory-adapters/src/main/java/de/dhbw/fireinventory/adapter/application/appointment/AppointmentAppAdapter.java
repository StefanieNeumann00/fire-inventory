package de.dhbw.fireinventory.adapter.application.appointment;

import de.dhbw.fireinventory.adapter.mapper.appointment.AppointmentResourceToAppointmentMapper;
import de.dhbw.fireinventory.adapter.mapper.appointment.AppointmentToAppointmentResourceMapper;
import de.dhbw.fireinventory.adapter.mapper.item.ItemResourceToItemMapper;
import de.dhbw.fireinventory.application.domain.service.appointment.AppointmentResource;
import de.dhbw.fireinventory.application.domain.service.appointment.AppointmentDomainServicePort;
import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.fireinventory.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentAppAdapter implements AppointmentApplicationAdapter{

    private final AppointmentDomainServicePort appointmentDomainServicePort;
    private final AppointmentToAppointmentResourceMapper appointmentToAppointmentResourceMapper;
    private final AppointmentResourceToAppointmentMapper appointmentResourceToAppointmentMapper;
    private final ItemResourceToItemMapper itemResourceToItemMapper;

    @Override
    public List<AppointmentResource> findAllSortedByDate() {
        return this.appointmentDomainServicePort.findAllSortedByDate().stream()
                .map(appointmentToAppointmentResourceMapper)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAppointment(AppointmentResource appointmentResource) {
        this.appointmentDomainServicePort.saveAppointment(appointmentResourceToAppointmentMapper.apply(appointmentResource));
    }

    @Override
    public void deleteAppointment(AppointmentResource appointmentResource) {
        this.appointmentDomainServicePort.deleteAppointment(appointmentResourceToAppointmentMapper.apply(appointmentResource));
    }

    @Override
    public List<AppointmentResource> findAllAppointmentsFor(ItemResource itemResource, String designation) {
        return this.appointmentDomainServicePort.findAllAppointmentsFor(itemResourceToItemMapper.apply(itemResource), designation).stream()
                .map(appointmentToAppointmentResourceMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<LocalDate> findAppointmentDatesFor(ItemResource itemResource, String designation) {
        return this.appointmentDomainServicePort.findAppointmentDatesFor(itemResourceToItemMapper.apply(itemResource), designation);
    }
}

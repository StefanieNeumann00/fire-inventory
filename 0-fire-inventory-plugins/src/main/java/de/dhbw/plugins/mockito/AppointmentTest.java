package de.dhbw.plugins.mockito;

import static org.mockito.Mockito.when;

import de.dhbw.fireinventory.application.domain.service.appointment.AppointmentApplicationService;
import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.appointment.AppointmentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentTest {

    @Mock
    AppointmentRepository appointmentRepository;

    AppointmentApplicationService applicationService;

    @Before
    public void initiate() {
        applicationService = new AppointmentApplicationService(appointmentRepository);
    }


    @Test
    public void testFindAppointmentByDesignation() {
        Appointment appointment = new Appointment();
        appointment.setDesignation("appointment");

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);

        when(appointmentRepository.findAppointmentsForDesignation("appointment")).thenReturn(appointments);

        List<Appointment> returnList = applicationService.findAllAppointmentsFor(null, "appointment");

        assertEquals(appointments, returnList);
    }
}

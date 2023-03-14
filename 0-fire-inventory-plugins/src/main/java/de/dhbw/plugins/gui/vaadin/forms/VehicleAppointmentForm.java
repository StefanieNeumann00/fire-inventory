package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import java.util.List;

public class VehicleAppointmentForm extends AppointmentForm{

    ComboBox<Vehicle> vehicleComboBox = new ComboBox<>("Vehicle");

    public VehicleAppointmentForm(List<Vehicle> vehicles) {
        binder = new BeanValidationBinder<>(Appointment.class);
        appointment = new Appointment();

        vehicleComboBox.setItems(vehicles);
        vehicleComboBox.setItemLabelGenerator(Vehicle::getDesignation);

        configureForm();

        add(appointmentDesignation, vehicleComboBox, dueDatePicker, createButtonsLayout());
    }

    public void setVehicle(Vehicle vehicle) {
        vehicleComboBox.setValue(vehicle);
    }
}

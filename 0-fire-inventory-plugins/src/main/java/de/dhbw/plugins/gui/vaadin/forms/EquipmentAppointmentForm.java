package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.equipment.Equipment;

import java.util.List;

public class EquipmentAppointmentForm extends AppointmentForm {

    ComboBox<Equipment> equipmentComboBox = new ComboBox<>("Equipment");

    public EquipmentAppointmentForm(List<Equipment> equipments) {
        binder = new BeanValidationBinder<>(Appointment.class);
        appointment = new Appointment();

        equipmentComboBox.setItems(equipments);
        equipmentComboBox.setItemLabelGenerator(Equipment::getDesignation);

        configureForm();

        add(appointmentDesignation, equipmentComboBox, dueDatePicker, createButtonsLayout());
    }

    public void setEquipment(Equipment equipment) {
        equipmentComboBox.setValue(equipment);
    }
}

package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.item.Item;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AppointmentForm extends FormLayout {

    ComboBox<Item> itemComboBox = new ComboBox<>("Item");
    TextField appointmentDesignation;
    DatePicker dueDatePicker;
    Button save;
    Button delete;
    Button cancel;
    Binder<Appointment> binder;
    protected Appointment appointment;

    public AppointmentForm(List<Item> items) {
        binder = new BeanValidationBinder<>(Appointment.class);
        appointment = new Appointment();

        itemComboBox.setItems(items);
        itemComboBox.setItemLabelGenerator(Item::getDesignation);

        configureForm();

        add(appointmentDesignation, itemComboBox, dueDatePicker, createButtonsLayout());
    }

    public void setItem(Item item) {
        itemComboBox.setValue(item);
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
        binder.readBean(appointment);
    }

    public void setDate(Date date) { this.dueDatePicker.setValue(date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()); }

    protected void configureForm() {
        appointmentDesignation = new TextField("Appointment Designation");
        dueDatePicker = new DatePicker("Due Date");

        binder.bind(appointmentDesignation, "designation");
        binder.bind(itemComboBox, "item");
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
    }

    protected HorizontalLayout createButtonsLayout() {
        save = new Button("Save");
        delete = new Button("delete");
        cancel = new Button("Cancel");

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new AppointmentForm.DeleteEvent(this, appointment)));
        cancel.addClickListener(event -> fireEvent(new AppointmentForm.CloseEvent(this)));

        HorizontalLayout layout = new HorizontalLayout(save, delete, cancel);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setSizeFull();
        layout.setSpacing(true);
        return layout;
    }

    public LocalDate getDueDate()
    {
        return dueDatePicker.getValue();
    }

    public void validateAndSave() {
        try {
            binder.writeBean(appointment);
            fireEvent(new AppointmentForm.SaveEvent(this, appointment));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class AppointmentFormEvent extends ComponentEvent<AppointmentForm> {
        private Appointment appointment;

        protected AppointmentFormEvent(AppointmentForm source, Appointment appointment) {
            super(source, false);
            this.appointment = appointment;
        }

        public Appointment getAppointment() {
            return appointment;
        }
    }

    public static class SaveEvent extends AppointmentForm.AppointmentFormEvent {
        SaveEvent(AppointmentForm source, Appointment appointment) {
            super(source, appointment);
        }
    }

    public static class DeleteEvent extends AppointmentForm.AppointmentFormEvent {
        DeleteEvent(AppointmentForm source, Appointment appointment) {
            super(source, appointment);
        }

    }

    public static class CloseEvent extends AppointmentForm.AppointmentFormEvent {
        CloseEvent(AppointmentForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

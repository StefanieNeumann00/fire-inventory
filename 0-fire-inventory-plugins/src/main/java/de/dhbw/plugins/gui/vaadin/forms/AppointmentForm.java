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
import de.dhbw.fireinventory.application.domain.service.appointment.AppointmentResource;
import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.item.Item;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AppointmentForm extends FormLayout {

    ComboBox<ItemResource> itemComboBox = new ComboBox<>("Gegenstand");
    TextField appointmentDesignation;
    DatePicker dueDatePicker;
    Button save;
    Button delete;
    Button cancel;
    Binder<AppointmentResource> binder;
    protected AppointmentResource appointmentResource;

    public AppointmentForm(List<ItemResource> itemResources) {
        binder = new BeanValidationBinder<>(AppointmentResource.class);
        appointmentResource = new AppointmentResource();

        itemComboBox.setItems(itemResources);
        itemComboBox.setItemLabelGenerator(ItemResource::getDesignation);

        configureForm();

        add(appointmentDesignation, itemComboBox, dueDatePicker, createButtonsLayout());
    }

    public void setItem(ItemResource itemResource) {
        itemComboBox.setValue(itemResource);
    }

    public void setAppointment(AppointmentResource appointmentResource) {
        this.appointmentResource = appointmentResource;
        binder.readBean(appointmentResource);
    }

    public void setDate(LocalDate date) { this.dueDatePicker.setValue(date); }

    protected void configureForm() {
        appointmentDesignation = new TextField("Terminbezeichnung");
        dueDatePicker = new DatePicker("Datum");

        binder.bind(appointmentDesignation, "designation");
        binder.bind(itemComboBox, "itemResource");
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
    }

    protected HorizontalLayout createButtonsLayout() {
        save = new Button("Speichern");
        delete = new Button("Löschen");
        cancel = new Button("Abbrechen");

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new AppointmentForm.DeleteEvent(this, appointmentResource)));
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
            binder.writeBean(appointmentResource);
            fireEvent(new AppointmentForm.SaveEvent(this, appointmentResource));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class AppointmentFormEvent extends ComponentEvent<AppointmentForm> {
        private AppointmentResource appointmentResource;

        protected AppointmentFormEvent(AppointmentForm source, AppointmentResource appointmentResource) {
            super(source, false);
            this.appointmentResource = appointmentResource;
        }

        public AppointmentResource getAppointmentResource() {
            return appointmentResource;
        }
    }

    public static class SaveEvent extends AppointmentForm.AppointmentFormEvent {
        SaveEvent(AppointmentForm source, AppointmentResource appointmentResource) {
            super(source, appointmentResource);
        }
    }

    public static class DeleteEvent extends AppointmentForm.AppointmentFormEvent {
        DeleteEvent(AppointmentForm source, AppointmentResource appointmentResource) {
            super(source, appointmentResource);
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

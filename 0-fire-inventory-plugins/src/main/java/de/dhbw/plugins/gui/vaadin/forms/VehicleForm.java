package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.application.domain.service.vehicle.VehicleResource;
import de.dhbw.fireinventory.domain.condition.Condition;

import java.util.List;

public class VehicleForm extends FormLayout {
    TextField designationTextField = new TextField("Bezeichnung");
    ComboBox<LocationResource> locationComboBox = new ComboBox<>("Abstellort");
    RadioButtonGroup<Condition> conditionRadioGroup = new RadioButtonGroup<>();
    Button save = new Button("Speichern");
    Button delete = new Button("Löschen");
    Button close = new Button("Abbrechen");
    Binder<VehicleResource> binder = new BeanValidationBinder<>(VehicleResource.class);
    private VehicleResource vehicleResource = new VehicleResource();

    public VehicleForm(List<LocationResource> locationResources) {
        locationComboBox.setItems(locationResources);
        locationComboBox.setItemLabelGenerator(LocationResource::getDesignation);
        createConditionRadioButton();

        this.createConditionRadioButton();
        add(designationTextField, locationComboBox, conditionRadioGroup,createButtonsLayout());
        binder.bind(designationTextField, "designation");
        binder.bind(locationComboBox, "locationResource");
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
    }

    private void createConditionRadioButton()
    {
        conditionRadioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        conditionRadioGroup.setLabel("Zustand");
        conditionRadioGroup.setItems(Condition.values());
        conditionRadioGroup.setValue(Condition.FUNKTIONSFÄHIG);
    }

    public void setVehicle(VehicleResource vehicleResource) {
        this.vehicleResource = vehicleResource;
        binder.readBean(vehicleResource);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new VehicleForm.DeleteEvent(this, vehicleResource)));
        close.addClickListener(event -> fireEvent(new VehicleForm.CloseEvent(this)));

        return new HorizontalLayout(save, delete, close);
    }

    public void validateAndSave() {
        try {
            binder.writeBean(vehicleResource);
            fireEvent(new SaveEvent(this, vehicleResource));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public Condition getConditionRadioButtonValue()
    {
        return conditionRadioGroup.getValue();
    }

    public static abstract class VehicleFormEvent extends ComponentEvent<VehicleForm> {
        private VehicleResource vehicleResource;

        protected VehicleFormEvent(VehicleForm source, VehicleResource vehicleResource) {
            super(source, false);
            this.vehicleResource = vehicleResource;
        }

        public VehicleResource getVehicleResource() {
            return vehicleResource;
        }
    }

    public static class SaveEvent extends VehicleForm.VehicleFormEvent {
        SaveEvent(VehicleForm source, VehicleResource vehicleResource) {
            super(source, vehicleResource);
        }
    }

    public static class DeleteEvent extends VehicleForm.VehicleFormEvent {
        DeleteEvent(VehicleForm source, VehicleResource vehicleResource) {
            super(source, vehicleResource);
        }

    }

    public static class CloseEvent extends VehicleForm.VehicleFormEvent {
        CloseEvent(VehicleForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

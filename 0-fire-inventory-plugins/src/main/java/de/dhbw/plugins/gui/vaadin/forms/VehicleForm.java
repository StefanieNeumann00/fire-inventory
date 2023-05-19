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
import de.dhbw.fireinventory.domain.Condition;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import java.util.List;

public class VehicleForm extends FormLayout {
    TextField designationTextField = new TextField("Designation");
    ComboBox<Location> locationComboBox = new ComboBox<>("Location");
    RadioButtonGroup<Condition> conditionRadioGroup = new RadioButtonGroup<>();
    Button save = new Button("Save");
    Button delete = new Button("delete");
    Button close = new Button("Cancel");
    Binder<Vehicle> binder = new BeanValidationBinder<>(Vehicle.class);
    private Vehicle vehicle = new Vehicle();

    public VehicleForm(List<Location> locations) {
        locationComboBox.setItems(locations);
        locationComboBox.setItemLabelGenerator(Location::getDesignation);
        createConditionRadioButton();

        this.createConditionRadioButton();
        add(designationTextField, locationComboBox, conditionRadioGroup,createButtonsLayout());
        binder.bind(designationTextField, "designation");
        binder.bind(locationComboBox, "place");
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
    }

    private void createConditionRadioButton()
    {
        conditionRadioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        conditionRadioGroup.setLabel("Zustand");
        conditionRadioGroup.setItems(Condition.values());
        conditionRadioGroup.setValue(Condition.FUNKTIONSFÄHIG);
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        binder.readBean(vehicle);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new VehicleForm.DeleteEvent(this, vehicle)));
        close.addClickListener(event -> fireEvent(new VehicleForm.CloseEvent(this)));

        return new HorizontalLayout(save, delete, close);
    }

    public void validateAndSave() {
        try {
            binder.writeBean(vehicle);
            fireEvent(new SaveEvent(this, vehicle));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public Condition getConditionRadioButtonValue()
    {
        return conditionRadioGroup.getValue();
    }

    public static abstract class VehicleFormEvent extends ComponentEvent<VehicleForm> {
        private Vehicle vehicle;

        protected VehicleFormEvent(VehicleForm source, Vehicle vehicle) {
            super(source, false);
            this.vehicle = vehicle;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }
    }

    public static class SaveEvent extends VehicleForm.VehicleFormEvent {
        SaveEvent(VehicleForm source, Vehicle vehicle) {
            super(source, vehicle);
        }
    }

    public static class DeleteEvent extends VehicleForm.VehicleFormEvent {
        DeleteEvent(VehicleForm source, Vehicle vehicle) {
            super(source, vehicle);
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

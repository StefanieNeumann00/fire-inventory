package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import java.util.List;

public class VehicleForm extends Dialog implements FormDialog {
    TextField designationTextField = new TextField("Designation");
    ComboBox<Place> placeComboBox = new ComboBox<>("Place");
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<Vehicle> binder = new BeanValidationBinder<>(Vehicle.class);
    private Vehicle vehicle = new Vehicle();

    public VehicleForm(List<Place> places) {
        this.setResizable(true);
        this.setDraggable(true);

        this.setHeaderTitle("Add Vehicle");

        placeComboBox.setItems(places);
        placeComboBox.setItemLabelGenerator(Place::getDesignation);

        add(this.createTextFieldLayout(),createButtonsLayout());
        binder.bind(designationTextField, "designation");
        binder.bind(placeComboBox, "place");
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        binder.readBean(vehicle);
    }

    private VerticalLayout createTextFieldLayout()
    {
        VerticalLayout textFieldLayout = new VerticalLayout();
        this.createConditionRadioButton();
        textFieldLayout.add(designationTextField, placeComboBox, conditionRadioGroup);
        textFieldLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return textFieldLayout;
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> closeDialog());

        return new HorizontalLayout(save, close);
    }

    public void validateAndSave() {
        try {
            binder.writeBean(vehicle);
            fireEvent(new SaveEvent(this, vehicle));
            this.close();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public String getConditionRadioButtonValue()
    {
        return conditionRadioGroup.getValue();
    }

    public void closeDialog()
    {
        this.close();
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

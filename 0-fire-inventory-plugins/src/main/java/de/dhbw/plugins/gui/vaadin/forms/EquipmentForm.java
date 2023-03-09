package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;

import java.util.List;

public class EquipmentForm extends FormLayout implements FormDialog {
        TextField designationTextField = new TextField("Designation");
        ComboBox<Location> locationComboBox = new ComboBox<>("Location");
        Button save = new Button("Save");
        Button delete = new Button("delete");
        Button cancel = new Button("Cancel");
        Binder<Equipment> binder = new BeanValidationBinder<>(Equipment.class);
        private Equipment equipment = new Equipment();

        public EquipmentForm(List<Location> locations) {

            locationComboBox.setItems(locations);
            locationComboBox.setItemLabelGenerator(Location::getDesignation);

            add(createTextFieldLayout(),createButtonsLayout());
            binder.bind(designationTextField, "designation");
            binder.bind(locationComboBox, "location");
            binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        }

        public void setEquipment(Equipment equipment) {
            this.equipment = equipment;
            binder.readBean(equipment);
        }

        private Component createTextFieldLayout()
        {
            VerticalLayout textFieldLayout = new VerticalLayout();
            this.createConditionRadioButton();
            textFieldLayout.add(designationTextField, locationComboBox, conditionRadioGroup);
            textFieldLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            return textFieldLayout;
        }

        private HorizontalLayout createButtonsLayout() {
            save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
            cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            save.addClickShortcut(Key.ENTER);
            cancel.addClickShortcut(Key.ESCAPE);

            save.addClickListener(event -> validateAndSave());
            delete.addClickListener(event -> fireEvent(new DeleteEvent(this, equipment)));
            cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

            return new HorizontalLayout(save, cancel);
        }

        public String getConditionRadioButtonValue()
        {
            return conditionRadioGroup.getValue();
        }

        public void validateAndSave() {
            try {
                binder.writeBean(equipment);
                fireEvent(new SaveEvent(this, equipment));
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }

    public static abstract class EquipmentFormEvent extends ComponentEvent<EquipmentForm> {
        private Equipment equipment;

        protected EquipmentFormEvent(EquipmentForm source, Equipment equipment) {
            super(source, false);
            this.equipment = equipment;
        }

        public Equipment getEquipment() {
            return equipment;
        }
    }

    public static class SaveEvent extends EquipmentFormEvent {
        SaveEvent(EquipmentForm source, Equipment equipment) {
            super(source, equipment);
        }
    }

    public static class DeleteEvent extends EquipmentFormEvent {
        DeleteEvent(EquipmentForm source, Equipment equipment) {
            super(source, equipment);
        }

    }

    public static class CloseEvent extends EquipmentFormEvent {
        CloseEvent(EquipmentForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

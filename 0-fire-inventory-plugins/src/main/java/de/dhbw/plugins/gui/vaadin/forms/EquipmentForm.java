package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import de.dhbw.fireinventory.adapter.location.LocationResource;
import de.dhbw.fireinventory.adapter.status.StatusResource;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.plugins.rest.EquipmentController;

import java.util.List;

public class EquipmentForm extends Dialog implements FormDialog {
        EquipmentController controller;
        TextField designation = new TextField("Designation");
        ComboBox<StatusResource> status = new ComboBox<>("Status");
        ComboBox<LocationResource> location = new ComboBox<>("Location");

        Binder<Equipment> binder = new BeanValidationBinder<>(Equipment.class);
        private Equipment equipment = new Equipment();

        public EquipmentForm(List<LocationResource> locations, List<StatusResource> statuses, EquipmentController controller) {
            this.controller = controller;
            this.setResizable(true);
            this.setDraggable(true);

            location.setItems(locations);
            location.setItemLabelGenerator(LocationResource::getDesignation);
            status.setItems(statuses);
            status.setItemLabelGenerator(StatusResource::getDesignation);

            add(createTextFieldLayout(),createButtonsLayout());
            binder.bindInstanceFields(this);
            binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        }

        public void setEquipment(Equipment equipment) {
            this.equipment = equipment;
            binder.readBean(equipment);
        }

        private Component createTextFieldLayout()
        {
            VerticalLayout textFieldLayout = new VerticalLayout();
            textFieldLayout.add(designation, location, status);
            textFieldLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            return textFieldLayout;
        }

        public void validateAndSave() {
            try {
                binder.writeBean(equipment);
                controller.addEquipment(equipment);
                this.close();
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }

        public void closeDialog()
        {
            this.close();
        }
}

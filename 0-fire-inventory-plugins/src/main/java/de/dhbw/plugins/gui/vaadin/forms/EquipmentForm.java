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
import de.dhbw.fireinventory.application.equipment.EquipmentApplicationService;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.plugins.rest.EquipmentController;

import java.util.List;

public class EquipmentForm extends Dialog implements FormDialog {
        EquipmentApplicationService service;
        TextField designation = new TextField("Designation");
        ComboBox<Status> status = new ComboBox<>("Status");
        ComboBox<Location> location = new ComboBox<>("Location");

        Binder<Equipment> binder = new BeanValidationBinder<>(Equipment.class);
        private Equipment equipment = new Equipment();

        public EquipmentForm(List<Location> locations, List<Status> statuses, EquipmentApplicationService service) {
            this.service = service;
            this.setResizable(true);
            this.setDraggable(true);

            location.setItems(locations);
            location.setItemLabelGenerator(Location::getDesignation);
            status.setItems(statuses);
            status.setItemLabelGenerator(Status::getDesignation);

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
                service.saveEquipment(equipment);
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

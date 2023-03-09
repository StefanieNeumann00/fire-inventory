package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import de.dhbw.fireinventory.application.equipment.EquipmentApplicationService;
import de.dhbw.fireinventory.application.status.StatusApplicationService;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;

import java.util.List;

public class EquipmentForm extends Dialog implements FormDialog {
        EquipmentApplicationService equipmentService;
        StatusApplicationService statusService;
        TextField designationTextField = new TextField("Designation");
        ComboBox<Location> locationComboBox = new ComboBox<>("Location");

        Binder<Equipment> binder = new BeanValidationBinder<>(Equipment.class);
        private Equipment equipment = new Equipment();

        public EquipmentForm(List<Location> locations, StatusApplicationService statusService, EquipmentApplicationService equipmentService) {
            this.equipmentService = equipmentService;
            this.statusService = statusService;
            this.setResizable(true);
            this.setDraggable(true);

            locationComboBox.setItems(locations);
            locationComboBox.setItemLabelGenerator(Location::getDesignation);

            add(createTextFieldLayout(),createButtonsLayout());
            binder.bind(designationTextField, "designation");
            binder.bind(locationComboBox, "location");
            binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        }

        private void setEquipmentStatus()
        {
            String conditionValue = conditionRadioGroup.getValue();
            Location location = equipment.getLocation();
            Status status = statusService.getStatusByDesignation("Nicht vor Ort");

            if(conditionValue == "funktionsfähig")
            {
                if(location.getVehicle() != null) {
                    String vehicleStatusDesignation = location.getVehicle().getStatus().getDesignation();

                    switch (vehicleStatusDesignation) {
                        case "Einsatzbereit":
                            status = statusService.getStatusByDesignation("Einsatzbereit");
                            break;
                        case "Kaputt":
                            status = statusService.getStatusByDesignation("Vor Ort");
                            break;
                    }
                }
                else if(location.getPlace() != null) {
                    String placeDesignation = location.getPlace().getDesignation();

                    if(placeDesignation != "Abwesend")
                    {
                        status = statusService.getStatusByDesignation("Vor Ort");
                    }
                }
            }
            else
            {
                status = statusService.getStatusByDesignation("Kaputt");

                if(location.getVehicle() != null) {
                    String vehicleStatusDesignation = location.getVehicle().getStatus().getDesignation();

                    if(vehicleStatusDesignation == "In Reparatur")
                    {
                        status = statusService.getStatusByDesignation("In Reparatur");
                    }
                }
            }

            equipment.setStatus(status);
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

        public void validateAndSave() {
            try {
                binder.writeBean(equipment);
                setEquipmentStatus();
                equipmentService.saveEquipment(equipment);
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

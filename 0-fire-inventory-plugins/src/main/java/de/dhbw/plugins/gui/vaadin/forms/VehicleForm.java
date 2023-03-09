package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import de.dhbw.fireinventory.application.status.StatusApplicationService;
import de.dhbw.fireinventory.application.vehicle.VehicleApplicationService;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import java.util.List;

public class VehicleForm extends Dialog implements FormDialog {
    VehicleApplicationService vehicleService;
    StatusApplicationService statusService;
    TextField designationTextField = new TextField("Designation");
    ComboBox<Place> placeComboBox = new ComboBox<>("Place");
    Binder<Vehicle> binder = new BeanValidationBinder<>(Vehicle.class);
    private Vehicle vehicle = new Vehicle();

    public VehicleForm(List<Place> places, StatusApplicationService statusService, VehicleApplicationService vehicleService) {
        this.vehicleService = vehicleService;
        this.statusService = statusService;
        this.setResizable(true);
        this.setDraggable(true);

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

    private void setVehicleStatus()
    {
        String conditionValue = conditionRadioGroup.getValue();
        Place place = vehicle.getPlace();
        Status status = statusService.getStatusByDesignation("Einsatzbereit");
        String placeDesignation = "";

        if(place != null) {
            placeDesignation = place.getDesignation();
        }

        if(conditionValue == "funktionsfähig")
        {
            if(placeDesignation == "Abwesend")
            {
                status = statusService.getStatusByDesignation("Nicht vor Ort");
            }
        }
        else
        {
            if(placeDesignation == "Abwesend")
            {
                status = statusService.getStatusByDesignation("In Reparatur");
            }
            else
            {
                status = statusService.getStatusByDesignation("Kaputt");
            }
        }

        vehicle.setStatus(status);
    }

    public void validateAndSave() {
        try {
            binder.writeBean(vehicle);
            setVehicleStatus();
            vehicleService.saveVehicle(vehicle);
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

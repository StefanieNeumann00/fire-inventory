package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import de.dhbw.fireinventory.application.vehicle.VehicleApplicationService;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import java.util.List;

public class VehicleForm extends Dialog implements FormDialog {
    VehicleApplicationService service;
    TextField designation = new TextField("Designation");
    ComboBox<Status> status = new ComboBox<>("Status");
    ComboBox<Place> place = new ComboBox<>("Place");
    Binder<Vehicle> binder = new BeanValidationBinder<>(Vehicle.class);
    private Vehicle vehicle = new Vehicle();

    public VehicleForm(List<Place> places, List<Status> statuses, VehicleApplicationService service) {
        this.service = service;
        this.setResizable(true);
        this.setDraggable(true);

        place.setItems(places);
        place.setItemLabelGenerator(Place::getDesignation);
        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getDesignation);

        add(this.createTextFieldLayout(),createButtonsLayout());
        binder.bindInstanceFields(this);
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        binder.readBean(vehicle);
    }

    private VerticalLayout createTextFieldLayout()
    {
        VerticalLayout textFieldLayout = new VerticalLayout();
        textFieldLayout.add(designation, place, status);
        textFieldLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return textFieldLayout;
    }

    public void validateAndSave() {
        try {
            binder.writeBean(vehicle);
            service.saveVehicle(vehicle);
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

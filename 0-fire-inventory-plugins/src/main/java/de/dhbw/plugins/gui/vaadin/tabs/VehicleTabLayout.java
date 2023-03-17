package de.dhbw.plugins.gui.vaadin.tabs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import de.dhbw.fireinventory.application.appointment.AppointmentApplicationService;
import de.dhbw.fireinventory.application.item.ItemApplicationService;
import de.dhbw.fireinventory.application.location.LocationApplicationService;
import de.dhbw.fireinventory.application.place.PlaceApplicationService;
import de.dhbw.fireinventory.application.status.StatusApplicationService;
import de.dhbw.fireinventory.application.vehicle.VehicleApplicationService;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import de.dhbw.plugins.gui.vaadin.components.ErrorMessage;
import de.dhbw.plugins.gui.vaadin.components.VehicleGrid;
import de.dhbw.plugins.gui.vaadin.forms.*;
import org.springframework.dao.DataIntegrityViolationException;

public class VehicleTabLayout extends AbstractTabLayout{

    VehicleGrid grid;
    TextField filterText = new TextField("Bezeichnung");
    HorizontalLayout filterLayout;
    VehicleForm vehicleForm;
    LocationApplicationService locationService;
    PlaceApplicationService placeService;
    StatusApplicationService statusService;
    VehicleApplicationService vehicleService;
    AppointmentApplicationService appointmentService;
    ItemApplicationService itemService;
    ComboBox<Place> placeComboBox;
    ComboBox<Status> statusComboBox;

    public VehicleTabLayout(LocationApplicationService locationService, ItemApplicationService itemService, PlaceApplicationService placeService, StatusApplicationService statusService, VehicleApplicationService vehicleService, AppointmentApplicationService appointmentService)
    {
        super();

        this.itemService = itemService;
        this.locationService = locationService;
        this.placeService = placeService;
        this.statusService = statusService;
        this.vehicleService = vehicleService;
        this.appointmentService = appointmentService;

        configureGrid();
        configureVehicleForm();

        add(getToolbar(), getContent());
        closeVehicleEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, vehicleForm);

        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, vehicleForm);

        content.addClassNames("content");
        content.setSizeFull();
        content.setHeightFull();

        return content;
    }

    private void configureVehicleForm() {
        vehicleForm = new VehicleForm(placeService.findAllBy(null));
        vehicleForm.setWidth("25em");
        vehicleForm.addListener(VehicleForm.SaveEvent.class, this::saveVehicle);
        vehicleForm.addListener(VehicleForm.DeleteEvent.class, this::deleteVehicle);
        vehicleForm.addListener(VehicleForm.CloseEvent.class, e -> closeVehicleEditor());
    }

    protected void configureGrid() {
        grid = new VehicleGrid(vehicleService);
        grid.addListener(VehicleGrid.AddAppointmentEvent.class, e -> addAppointment(e.getVehicle()));
        grid.addListener(VehicleGrid.EditVehicleEvent.class, e -> editVehicle(e.getVehicle()));
    }

    private VerticalLayout getToolbar() {
        filterText.setPlaceholder("...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Icon filterIcon = new Icon(VaadinIcon.FILTER);
        filterIcon.addClickListener(e -> changeFilterVisibility());

        Button addVehicleButton = new Button("Add Vehicle");

        placeComboBox = new ComboBox<>("Place");
        placeComboBox.setItems(placeService.findAllBy(null));
        placeComboBox.setItemLabelGenerator(Place::getDesignation);
        placeComboBox.setClearButtonVisible(true);
        placeComboBox.addValueChangeListener(e -> updateList());

        statusComboBox = new ComboBox<>("Status");
        statusComboBox.setItems(statusService.findAllStatuses());
        statusComboBox.setItemLabelGenerator(Status::getDesignation);
        statusComboBox.setClearButtonVisible(true);
        statusComboBox.addValueChangeListener(e -> updateList());

        addVehicleButton.addClickListener(event -> addVehicle());

        HorizontalLayout buttonsLayout = new HorizontalLayout(filterIcon, addVehicleButton);
        buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        filterLayout = new HorizontalLayout(placeComboBox, statusComboBox, filterText);
        filterLayout.setVisible(false);

        VerticalLayout toolbar = new VerticalLayout(buttonsLayout, filterLayout);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addAppointment(Vehicle vehicle) {}

    private void changeFilterVisibility() {
        if(filterLayout.isVisible()) { filterLayout.setVisible(false); }
        else { filterLayout.setVisible(true); }
    }

    public void setStatusComboBox(Status status) { statusComboBox.setValue(status); }

    private void deleteVehicle(VehicleForm.DeleteEvent event) {
        try {
            locationService.deleteLocation(event.getVehicle());
            itemService.deleteItem(event.getVehicle());
            vehicleService.deleteVehicle(event.getVehicle());
            updateList();
            closeVehicleEditor();
            fireEvent(new GridUpdatedEvent(this));
        }
        catch (DataIntegrityViolationException exception) {
            new ErrorMessage("You cannot delete this vehicle since there are still equipments attached.");
        }

    }

    public void editVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            closeVehicleEditor();
        } else {
            vehicleForm.setVehicle(vehicle);
            vehicleForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeVehicleEditor() {
        vehicleForm.setVehicle(null);
        vehicleForm.setVisible(false);
        removeClassName("editing");
    }

    private void addVehicle() {
        grid.clearGridSelection();
        editVehicle(new Vehicle());
    }

    private void updateList() {
        grid.updateList(placeComboBox.getValue(), statusComboBox.getValue(), filterText.getValue());
    }

    private void saveVehicle(VehicleForm.SaveEvent event) {
        Vehicle vehicle = event.getVehicle();
        vehicle = setVehicleStatus(vehicle);
        vehicleService.saveVehicle(vehicle);
        updateList();
        closeVehicleEditor();
        fireEvent(new GridUpdatedEvent(this));
    }

    private Vehicle setVehicleStatus(Vehicle vehicle)
    {
        String conditionValue = vehicleForm.getConditionRadioButtonValue();
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
        return vehicle;
    }

}

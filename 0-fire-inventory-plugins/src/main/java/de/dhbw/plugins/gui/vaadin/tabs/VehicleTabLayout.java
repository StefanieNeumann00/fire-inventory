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
import de.dhbw.fireinventory.application.vehicle.VehicleApplicationService;
import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import de.dhbw.plugins.gui.vaadin.components.ErrorMessage;
import de.dhbw.plugins.gui.vaadin.components.VehicleGrid;
import de.dhbw.plugins.gui.vaadin.forms.*;
import de.dhbw.plugins.gui.vaadin.routes.Calendar;
import org.springframework.dao.DataIntegrityViolationException;

public class VehicleTabLayout extends AbstractTabLayout{

    VehicleGrid grid;
    TextField filterText = new TextField("Bezeichnung");
    HorizontalLayout filterLayout;
    VehicleForm vehicleForm;
    LocationApplicationService locationService;
    VehicleApplicationService vehicleService;
    AppointmentApplicationService appointmentService;
    ItemApplicationService itemService;
    ComboBox<Location> locationComboBox;
    ComboBox<Status> statusComboBox;

    public VehicleTabLayout(LocationApplicationService locationService, ItemApplicationService itemService, VehicleApplicationService vehicleService, AppointmentApplicationService appointmentService)
    {
        super();

        this.itemService = itemService;
        this.locationService = locationService;
        this.vehicleService = vehicleService;
        this.appointmentService = appointmentService;

        configureGrid();
        configureVehicleForm();

        add(getToolbar(), getContent());
        closeVehicleEditor();
        updateList();
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
        vehicleForm = new VehicleForm(locationService.findAllPlaces(null));
        vehicleForm.setWidth("25em");
        vehicleForm.addListener(VehicleForm.SaveEvent.class, this::saveVehicle);
        vehicleForm.addListener(VehicleForm.DeleteEvent.class, this::deleteVehicle);
        vehicleForm.addListener(VehicleForm.CloseEvent.class, e -> closeVehicleEditor());
    }

    protected void configureGrid() {
        grid = new VehicleGrid(vehicleService);
        grid.addListener(VehicleGrid.AddAppointmentEvent.class, e -> addAppointment(e.getVehicle()));
        grid.addListener(VehicleGrid.EditVehicleEvent.class, e -> editVehicle(e.getVehicle()));
        grid.addListener(VehicleGrid.VehicleConditionChangedEvent.class, e -> changeCondition(e.getVehicle()));
    }

    private void changeCondition(Vehicle vehicle) {
        vehicleService.changeCondition(vehicle);
        updateList();
    }

    private VerticalLayout getToolbar() {
        filterText.setPlaceholder("...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Icon filterIcon = new Icon(VaadinIcon.FILTER);
        filterIcon.addClickListener(e -> changeFilterVisibility());

        Button addVehicleButton = new Button("Add Vehicle");

        locationComboBox = new ComboBox<>("Place");
        locationComboBox.setItems(locationService.findAllPlaces(null));
        locationComboBox.setItemLabelGenerator(Location::getDesignation);
        locationComboBox.setClearButtonVisible(true);
        locationComboBox.addValueChangeListener(e -> updateList());

        statusComboBox = new ComboBox<>("Status");
        statusComboBox.setItems(Status.values());
        statusComboBox.setItemLabelGenerator(Status::getDesignation);
        statusComboBox.setClearButtonVisible(true);
        statusComboBox.addValueChangeListener(e -> updateList());

        addVehicleButton.addClickListener(event -> addVehicle());

        HorizontalLayout buttonsLayout = new HorizontalLayout(filterIcon, addVehicleButton);
        buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        filterLayout = new HorizontalLayout(locationComboBox, statusComboBox, filterText);
        filterLayout.setVisible(false);

        VerticalLayout toolbar = new VerticalLayout(buttonsLayout, filterLayout);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addAppointment(Vehicle vehicle) {
        Appointment appointment = new Appointment();
        appointment.setItem(vehicle);
        this.getUI().ifPresent(ui -> ui.navigate(Calendar.class).ifPresent(calendar -> calendar.editAppointment(appointment)));
    }

    private void changeFilterVisibility() {
        if(filterLayout.isVisible()) { filterLayout.setVisible(false); }
        else { filterLayout.setVisible(true); }
    }

    public void setStatusComboBox(Status status) { statusComboBox.setValue(status); }

    private void deleteVehicle(VehicleForm.DeleteEvent event) {
        try {
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
        grid.updateList(locationComboBox.getValue(), statusComboBox.getValue(), filterText.getValue());
    }

    private void saveVehicle(VehicleForm.SaveEvent event) {
        Vehicle vehicle = event.getVehicle();
        Condition conditionValue = vehicleForm.getConditionRadioButtonValue();
        vehicleService.saveVehicle(vehicle, conditionValue);
        updateList();
        closeVehicleEditor();
        fireEvent(new GridUpdatedEvent(this));
    }

}

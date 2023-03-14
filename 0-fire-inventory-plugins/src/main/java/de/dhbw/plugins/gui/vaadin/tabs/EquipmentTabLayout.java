package de.dhbw.plugins.gui.vaadin.tabs;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.application.appointment.AppointmentApplicationService;
import de.dhbw.fireinventory.application.equipment.EquipmentApplicationService;
import de.dhbw.fireinventory.application.location.LocationApplicationService;
import de.dhbw.fireinventory.application.place.PlaceApplicationService;
import de.dhbw.fireinventory.application.status.StatusApplicationService;
import de.dhbw.fireinventory.application.vehicle.VehicleApplicationService;
import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import de.dhbw.plugins.gui.vaadin.MainLayout;
import de.dhbw.plugins.gui.vaadin.components.EquipmentGrid;
import de.dhbw.plugins.gui.vaadin.forms.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.util.Date;

public class EquipmentTabLayout extends AbstractTabLayout {
    EquipmentGrid grid;
    TextField filterText = new TextField("Bezeichnung");
    HorizontalLayout filterLayout;
    EquipmentForm equipmentForm;
    EquipmentAppointmentForm appointmentForm;
    EquipmentApplicationService equipmentService;
    LocationApplicationService locationService;
    StatusApplicationService statusService;
    AppointmentApplicationService appointmentService;
    ComboBox<Location> locationComboBox;
    ComboBox<Status> statusComboBox;

    public EquipmentTabLayout(EquipmentApplicationService equipmentService, LocationApplicationService locationService, StatusApplicationService statusService, AppointmentApplicationService appointmentService)
    {
        super();

        this.equipmentService = equipmentService;
        this.locationService = locationService;
        this.statusService = statusService;
        this.appointmentService = appointmentService;

        configureGrid();
        configureEquipmentForm();
        configureAppointmentForm();

        add(getToolbar(), getContent());
        closeEquipmentEditor();
        closeAppointmentEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, equipmentForm, appointmentForm);

        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, equipmentForm);
        content.setFlexGrow(1,appointmentForm);

        content.addClassNames("content");
        content.setSizeFull();
        content.setHeightFull();

        return content;
    }

    private void configureEquipmentForm() {
        equipmentForm = new EquipmentForm(locationService.findAllLocations());
        equipmentForm.setWidth("25em");
        equipmentForm.addListener(EquipmentForm.SaveEvent.class, this::saveEquipment);
        equipmentForm.addListener(EquipmentForm.DeleteEvent.class, this::deleteEquipment);
        equipmentForm.addListener(EquipmentForm.CloseEvent.class, e -> closeEquipmentEditor());
    }

    private void configureAppointmentForm() {
        appointmentForm = new EquipmentAppointmentForm(equipmentService.findAllEquipments(null));
        appointmentForm.setWidth("25em");
        appointmentForm.addListener(AppointmentForm.SaveEvent.class, this::saveAppointment);
        appointmentForm.addListener(AppointmentForm.CloseEvent.class, e -> closeAppointmentEditor());
    }

    protected void configureGrid() {
        grid = new EquipmentGrid(equipmentService);
        grid.addListener(EquipmentGrid.AddAppointmentEvent.class, e -> addAppointment(e.getEquipment()));
        grid.addListener(EquipmentGrid.EditEquipmentEvent.class, e -> editEquipment(e.getEquipment()));
    }

    private VerticalLayout getToolbar() {
        filterText.setPlaceholder("...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Icon filterIcon = new Icon(VaadinIcon.FILTER);
        filterIcon.addClickListener(e -> changeFilterVisibility());

        Button addEquipmentButton = new Button("Add Equipment");

        locationComboBox = new ComboBox<>("Location");
        locationComboBox.setItems(locationService.findAllLocations());
        locationComboBox.setItemLabelGenerator(Location::getDesignation);
        locationComboBox.setClearButtonVisible(true);
        locationComboBox.addValueChangeListener(e -> updateList());

        statusComboBox = new ComboBox<>("Status");
        statusComboBox.setItems(statusService.findAllStatuses());
        statusComboBox.setItemLabelGenerator(Status::getDesignation);
        statusComboBox.setClearButtonVisible(true);
        statusComboBox.addValueChangeListener(e -> updateList());

        addEquipmentButton.addClickListener(event -> addEquipment());

        HorizontalLayout buttonsLayout = new HorizontalLayout(filterIcon, addEquipmentButton);
        buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        filterLayout = new HorizontalLayout(locationComboBox, statusComboBox, filterText);
        filterLayout.setVisible(false);

        VerticalLayout toolbar = new VerticalLayout(buttonsLayout, filterLayout);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void changeFilterVisibility() {
        if(filterLayout.isVisible()) { filterLayout.setVisible(false); }
        else { filterLayout.setVisible(true); }
    }

    public void setStatusComboBox(Status status) { statusComboBox.setValue(status); }

    public void editEquipment(Equipment equipment) {
        closeAppointmentEditor();
        if (equipment == null) {
            closeEquipmentEditor();
        } else {
            equipmentForm.setEquipment(equipment);
            equipmentForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEquipmentEditor() {
        equipmentForm.setEquipment(null);
        equipmentForm.setVisible(false);
        removeClassName("editing");
    }

    private void closeAppointmentEditor() {
        appointmentForm.setEquipment(null);
        appointmentForm.setVisible(false);
        removeClassName("editing");
    }

    private void addEquipment() {
        grid.clearGridSelection();
        editEquipment(new Equipment());
    }

    private void addAppointment(Equipment equipment) {
        closeEquipmentEditor();
        appointmentForm.setEquipment(equipment);
        appointmentForm.setVisible(true);
        addClassName("editing");
    }

    private void updateList() {
        grid.updateList(locationComboBox.getValue(), statusComboBox.getValue(), filterText.getValue());
    }

    private void saveEquipment(EquipmentForm.SaveEvent event) {
        Equipment equipment = event.getEquipment();
        equipment = setEquipmentStatus(equipment);
        equipmentService.saveEquipment(equipment);
        updateList();
        closeEquipmentEditor();
        fireEvent(new GridUpdatedEvent(this));
    }

    private void saveAppointment(AppointmentForm.SaveEvent event) {
        Appointment appointment = event.getAppointment();
        Date date = Date.from(appointmentForm.getDueDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        appointment.setDueDate(date);
        appointmentService.saveAppointment(appointment);
        updateList();
        closeAppointmentEditor();
    }

    private void deleteEquipment(EquipmentForm.DeleteEvent event) {
        equipmentService.deleteEquipment(event.getEquipment());
        updateList();
        closeEquipmentEditor();
    }

    private Equipment setEquipmentStatus(Equipment equipment)
    {
        String conditionValue = equipmentForm.getConditionRadioButtonValue();
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
        return equipment;
    }
}

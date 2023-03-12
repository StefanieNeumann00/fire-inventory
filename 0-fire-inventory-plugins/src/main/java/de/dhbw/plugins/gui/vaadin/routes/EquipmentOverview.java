package de.dhbw.plugins.gui.vaadin.routes;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
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
import de.dhbw.plugins.gui.vaadin.forms.AppointmentForm;
import de.dhbw.plugins.gui.vaadin.forms.EquipmentForm;
import de.dhbw.plugins.gui.vaadin.forms.VehicleForm;
import de.dhbw.plugins.gui.vaadin.forms.PlaceForm;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.util.Date;

@Route(value="", layout = MainLayout.class)
@PageTitle("Overview | FireInventory")
public class EquipmentOverview extends VerticalLayout {
    Grid<Equipment> grid = new Grid<>(Equipment.class, false);
    TextField filterText = new TextField();
    EquipmentForm equipmentForm;
    PlaceForm placeForm;
    VehicleForm vehicleForm;
    AppointmentForm appointmentForm;
    EquipmentApplicationService equipmentService;
    LocationApplicationService locationService;
    PlaceApplicationService placeService;
    StatusApplicationService statusService;
    VehicleApplicationService vehicleService;
    AppointmentApplicationService appointmentService;
    VerticalLayout forms;

    @Autowired
    public EquipmentOverview(EquipmentApplicationService equipmentService, LocationApplicationService locationService, PlaceApplicationService placeService, StatusApplicationService statusService, VehicleApplicationService vehicleService, AppointmentApplicationService appointmentService)
    {
        this.equipmentService = equipmentService;
        this.locationService = locationService;
        this.placeService = placeService;
        this.statusService = statusService;
        this.vehicleService = vehicleService;
        this.appointmentService = appointmentService;

        addClassName("overview");
        setSizeFull();
        configureGrid();
        configureEquipmentForm();
        configurePlaceForm();
        configureVehicleForm();
        configureAppointmentForm();

        add(getToolbar(), getContent());
        updateList();
        closeEquipmentEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, equipmentForm, appointmentForm);

        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, equipmentForm);
        content.setFlexGrow(1,appointmentForm);

        content.addClassNames("content");
        content.setSizeFull();

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
        appointmentForm = new AppointmentForm(equipmentService.findAllEquipments(null));
        appointmentForm.setWidth("25em");
        appointmentForm.addListener(AppointmentForm.SaveEvent.class, this::saveAppointment);
        appointmentForm.addListener(AppointmentForm.CloseEvent.class, e -> closeAppointmentEditor());
    }

    private void configurePlaceForm() {
        placeForm = new PlaceForm();
        placeForm.addListener(PlaceForm.SaveEvent.class, this::savePlace);
        placeForm.addListener(PlaceForm.CloseEvent.class, e -> closePlaceDialog());
    }

    private void configureVehicleForm() {
        vehicleForm = new VehicleForm(placeService.findAllPlaces());
        vehicleForm.addListener(VehicleForm.SaveEvent.class, this::saveVehicle);
        vehicleForm.addListener(VehicleForm.CloseEvent.class, e -> closeVehicleDialog());
    }

    private void configureGrid() {
        grid.addClassNames("equipment-grid");
        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addColumn(Equipment::getDesignation).setHeader("Bezeichnung");
        grid.addColumn(e -> e.getLocation().getDesignation()).setHeader("Ablageort");
        grid.addColumn(e -> e.getStatus().getDesignation()).setHeader("Status");
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, equipment) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(event -> addAppointment(equipment));
                    button.setIcon(new Icon(VaadinIcon.CALENDAR));
                })).setHeader("Manage");

        grid.asSingleSelect().addValueChangeListener(event ->
                editEquipment(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addEquipmentButton = new Button("Add Equipment");
        Button addVehicleButton = new Button("Add Vehicle");
        Button addPlaceButton = new Button("Add Place");

        addEquipmentButton.addClickListener(event -> addEquipment());
        addVehicleButton.addClickListener(event -> vehicleForm.open());
        addPlaceButton.addClickListener(event -> placeForm.open());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addEquipmentButton, addVehicleButton, addPlaceButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

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

    private void closePlaceDialog() {
        placeForm.close();
    }

    private void closeVehicleDialog() {
        vehicleForm.close();
    }

    private void addEquipment() {
        grid.asSingleSelect().clear();
        editEquipment(new Equipment());
    }

    private void addAppointment(Equipment equipment) {
        closeEquipmentEditor();
        appointmentForm.setEquipment(equipment);
        appointmentForm.setVisible(true);
        addClassName("editing");
    }

    private void updateList() {
        grid.setItems(equipmentService.findAllEquipments(filterText.getValue()));
    }

    private void saveEquipment(EquipmentForm.SaveEvent event) {
        Equipment equipment = event.getEquipment();
        equipment = setEquipmentStatus(equipment);
        equipmentService.saveEquipment(equipment);
        updateList();
        closeEquipmentEditor();
    }

    private void saveAppointment(AppointmentForm.SaveEvent event) {
        Appointment appointment = event.getAppointment();
        Date date = Date.from(appointmentForm.getDueDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        appointment.setDueDate(date);
        appointmentService.saveAppointment(appointment);
        updateList();
        closeAppointmentEditor();
    }

    private void savePlace(PlaceForm.SaveEvent event) {
        Place place = event.getPlace();
        placeService.savePlace(place);
        updateList();
        closePlaceDialog();
    }

    private void saveVehicle(VehicleForm.SaveEvent event) {
        Vehicle vehicle = event.getVehicle();
        vehicle = setVehicleStatus(vehicle);
        vehicleService.saveVehicle(vehicle);
        updateList();
        closeVehicleDialog();
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

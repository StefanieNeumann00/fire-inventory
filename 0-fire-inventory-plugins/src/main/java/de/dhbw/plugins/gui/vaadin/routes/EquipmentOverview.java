package de.dhbw.plugins.gui.vaadin.routes;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import de.dhbw.fireinventory.adapter.equipment.EquipmentResource;
import de.dhbw.fireinventory.application.equipment.EquipmentApplicationService;
import de.dhbw.fireinventory.application.location.LocationApplicationService;
import de.dhbw.fireinventory.application.place.PlaceApplicationService;
import de.dhbw.fireinventory.application.status.StatusApplicationService;
import de.dhbw.fireinventory.application.vehicle.VehicleApplicationService;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.plugins.gui.vaadin.MainLayout;
import de.dhbw.plugins.gui.vaadin.forms.EquipmentForm;
import de.dhbw.plugins.gui.vaadin.forms.PlaceForm;
import de.dhbw.plugins.gui.vaadin.forms.VehicleForm;
import de.dhbw.plugins.rest.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value="", layout = MainLayout.class)
@PageTitle("Overview | FireInventory")
public class EquipmentOverview extends VerticalLayout {
    Grid<Equipment> grid = new Grid<>(Equipment.class, false);
    TextField filterText = new TextField();
    EquipmentForm equipmentForm;
    PlaceForm placeForm;
    VehicleForm vehicleForm;
    EquipmentApplicationService equipmentService;
    LocationApplicationService locationService;
    PlaceApplicationService placeService;
    StatusApplicationService statusService;
    VehicleApplicationService vehicleService;

    @Autowired
    public EquipmentOverview(EquipmentApplicationService equipmentService, LocationApplicationService locationService, PlaceApplicationService placeService, StatusApplicationService statusService, VehicleApplicationService vehicleService)
    {
        this.equipmentService = equipmentService;
        this.locationService = locationService;
        this.placeService = placeService;
        this.statusService = statusService;
        this.vehicleService = vehicleService;
        addClassName("overview");
        setSizeFull();
        configureGrid();
        configureEquipmentForm();

        add(getToolbar(), getContent());
        updateList();
        closeEquipmentEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, equipmentForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, equipmentForm);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        grid.addColumn(Equipment::getDesignation).setHeader("Bezeichnung");
        grid.addColumn(e -> e.getLocation().getDesignation()).setHeader("Ablageort");
        grid.addColumn(e -> e.getStatus().getDesignation()).setHeader("Status");
        grid.addClassNames("equipment-grid");
        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

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
        if (equipment == null) {
            closeEquipmentEditor();
        } else {
            equipmentForm.setEquipment(equipment);
            equipmentForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void addEquipment() {
        grid.asSingleSelect().clear();
        editEquipment(new Equipment());
    }

    private void updateList() {
        grid.setItems(equipmentService.findAllEquipments());
    }

    private void configureEquipmentForm() {
        equipmentForm = new EquipmentForm(locationService.findAllLocations());
        equipmentForm.setWidth("25em");
        equipmentForm.addListener(EquipmentForm.SaveEvent.class, this::saveEquipment);
        equipmentForm.addListener(EquipmentForm.DeleteEvent.class, this::deleteEquipment);
        equipmentForm.addListener(EquipmentForm.CloseEvent.class, e -> closeEquipmentEditor());
    }

    private void saveEquipment(EquipmentForm.SaveEvent event) {
        Equipment equipment = event.getEquipment();
        equipment = setEquipmentStatus(equipment);
        equipmentService.saveEquipment(equipment);
        updateList();
        closeEquipmentEditor();
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

    private void closeEquipmentEditor() {
        equipmentForm.setEquipment(null);
        equipmentForm.setVisible(false);
        removeClassName("editing");
    }
}

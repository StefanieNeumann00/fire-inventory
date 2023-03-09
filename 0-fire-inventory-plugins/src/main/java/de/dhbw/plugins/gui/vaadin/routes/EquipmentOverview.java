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
    Dialog form;
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

        add(getToolbar(), getContent());
        updateList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(2, grid);
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

        addEquipmentButton.addClickListener(event -> openFormDialog("Equipment"));
        addVehicleButton.addClickListener(event -> openFormDialog("Vehicle"));
        addPlaceButton.addClickListener(event -> openFormDialog("Place"));

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addEquipmentButton, addVehicleButton, addPlaceButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setItems(equipmentService.findAllEquipments());
    }

    private void openFormDialog(String object)
    {
        switch (object) {
            case "Equipment":
                form = new EquipmentForm(locationService.findAllLocations(), statusService.findAllStatuses(), equipmentService);
                break;
            case "Vehicle":
                form = new VehicleForm(placeService.findAllPlaces(), statusService.findAllStatuses(), vehicleService);
                break;
            case "Place":
                form = new PlaceForm(placeService);
                break;
        }
        form.open();
    }
}

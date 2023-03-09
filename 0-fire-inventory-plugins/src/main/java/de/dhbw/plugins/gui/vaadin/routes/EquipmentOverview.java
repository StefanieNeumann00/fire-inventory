package de.dhbw.plugins.gui.vaadin;

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
import de.dhbw.fireinventory.adapter.equipment.EquipmentResource;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.plugins.gui.vaadin.forms.EquipmentForm;
import de.dhbw.plugins.gui.vaadin.forms.PlaceForm;
import de.dhbw.plugins.gui.vaadin.forms.VehicleForm;
import de.dhbw.plugins.rest.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value="", layout = MainLayout.class)
@PageTitle("Overview | FireInventory")
public class EquipmentOverview extends VerticalLayout {
    Grid<EquipmentResource> grid = new Grid<>(EquipmentResource.class, false);
    TextField filterText = new TextField();
    Dialog form;
    EquipmentController equipmentController;
    LocationController locationController;
    PlaceController placeController;
    StatusController statusController;
    VehicleController vehicleController;

    @Autowired
    public EquipmentOverview(EquipmentController equipmentController, LocationController locationController, PlaceController placeController, StatusController statusController, VehicleController vehicleController)
    {
        this.vehicleController = vehicleController;
        this.equipmentController = equipmentController;
        this.locationController = locationController;
        this.statusController = statusController;
        this.placeController = placeController;
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
        grid.addColumn(EquipmentResource::getDesignation).setHeader("Bezeichnung");
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
        grid.setItems(equipmentController.getEquipments());
    }

    private void openFormDialog(String object)
    {
        switch (object) {
            case "Equipment":
                form = new EquipmentForm(locationController.getLocations(), statusController.getStatuses(), equipmentController);
                break;
            case "Vehicle":
                form = new VehicleForm(placeController.getPlaces(), statusController.getStatuses(), vehicleController);
                break;
            case "Place":
                form = new PlaceForm(placeController);
                break;
        }
        form.open();
    }
}

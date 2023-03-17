package de.dhbw.plugins.gui.vaadin.routes;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.dhbw.fireinventory.application.appointment.AppointmentApplicationService;
import de.dhbw.fireinventory.application.equipment.EquipmentApplicationService;
import de.dhbw.fireinventory.application.item.ItemApplicationService;
import de.dhbw.fireinventory.application.location.LocationApplicationService;
import de.dhbw.fireinventory.application.place.PlaceApplicationService;
import de.dhbw.fireinventory.application.status.StatusApplicationService;
import de.dhbw.fireinventory.application.vehicle.VehicleApplicationService;
import de.dhbw.plugins.gui.vaadin.MainLayout;
import de.dhbw.plugins.gui.vaadin.tabs.EquipmentTabLayout;
import de.dhbw.plugins.gui.vaadin.tabs.PlaceTabLayout;
import de.dhbw.plugins.gui.vaadin.tabs.VehicleTabLayout;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value="", layout = MainLayout.class)
@PageTitle("Overview | FireInventory")
public class Inventory extends VerticalLayout {

    EquipmentApplicationService equipmentService;
    LocationApplicationService locationService;
    PlaceApplicationService placeService;
    StatusApplicationService statusService;
    VehicleApplicationService vehicleService;
    AppointmentApplicationService appointmentService;
    ItemApplicationService itemService;

    EquipmentTabLayout equipmentTabLayout;
    VehicleTabLayout vehicleTabLayout;
    PlaceTabLayout placeTabLayout;

    Span equipmentBadge;
    Span vehicleBadge;

    Tab equipmentTab;
    Tab vehiclesTab;
    Tab placeTab;

    @Autowired
    public Inventory(EquipmentApplicationService equipmentService, ItemApplicationService itemService, LocationApplicationService locationService, PlaceApplicationService placeService, StatusApplicationService statusService, VehicleApplicationService vehicleService, AppointmentApplicationService appointmentService) {
        this.equipmentService = equipmentService;
        this.locationService = locationService;
        this.placeService = placeService;
        this.statusService = statusService;
        this.vehicleService = vehicleService;
        this.appointmentService = appointmentService;
        this.itemService = itemService;

        setSizeFull();
        setHeightFull();

        equipmentTab = new Tab(new Span("Equipments"));
        vehiclesTab = new Tab(new Span("Vehicles"));
        placeTab = new Tab(new Span("Places"));

        configureLayouts();
        equipmentBadge = createBadge(equipmentService.equipmentStatusCount("Kaputt"));
        equipmentBadge.addClickListener(e -> navigateToDestroyedEquipments());
        vehicleBadge = createBadge(vehicleService.vehicleStatusCount("Kaputt"));
        vehicleBadge.addClickListener(e -> navigateToDestroyedVehicles());
        updateBadge();

        Tabs tabs = new Tabs(equipmentTab, vehiclesTab, placeTab);
        tabs.addSelectedChangeListener(
                event -> setContent(event.getSelectedTab()));
        add(tabs, equipmentTabLayout);
    }

    private void configureLayouts() {
        equipmentTabLayout = new EquipmentTabLayout(equipmentService, locationService, statusService, itemService);
        equipmentTabLayout.addListener(EquipmentTabLayout.GridUpdatedEvent.class, e -> updateBadge());

        vehicleTabLayout = new VehicleTabLayout(locationService, itemService, placeService, statusService, vehicleService, appointmentService);
        equipmentTabLayout.addListener(EquipmentTabLayout.GridUpdatedEvent.class, e -> updateBadge());

        placeTabLayout = new PlaceTabLayout(locationService, placeService);
    }

    private void navigateToDestroyedEquipments() {
        equipmentTab.setSelected(true);
        equipmentTabLayout.setStatusComboBox(statusService.getStatusByDesignation("Kaputt"));
    }

    private void navigateToDestroyedVehicles() {
        vehiclesTab.setSelected(true);
        vehicleTabLayout.setStatusComboBox(statusService.getStatusByDesignation("Kaputt"));
    }

    private void updateBadge() {
        int destroyedEquipmentCount = equipmentService.equipmentStatusCount("Kaputt");
        int destroyedVehicleCount = vehicleService.vehicleStatusCount("Kaputt");

        equipmentTab.remove(equipmentBadge);
        vehiclesTab.remove(vehicleBadge);

        equipmentBadge.setText(String.valueOf(destroyedEquipmentCount));
        vehicleBadge.setText(String.valueOf(destroyedVehicleCount));


        if (destroyedEquipmentCount != 0) {
            equipmentTab.add(equipmentBadge);
        }

        if (destroyedVehicleCount != 0) {
            vehiclesTab.add(vehicleBadge);
        }
    }

    private Span createBadge(int value) {
        Span badge = new Span(String.valueOf(value));
        badge.getElement().getThemeList().add("badge error small");
        badge.getStyle().set("margin-inline-start", "var(--lumo-space-xs)");
        return badge;
    }

    private void setContent(Tab tab) {
        remove(equipmentTabLayout, placeTabLayout, vehicleTabLayout);

        if (tab.equals(equipmentTab)) {
            add(equipmentTabLayout);
        } else if (tab.equals(vehiclesTab)) {
            add(vehicleTabLayout);
        } else {
            add(placeTabLayout);
        }
    }
}

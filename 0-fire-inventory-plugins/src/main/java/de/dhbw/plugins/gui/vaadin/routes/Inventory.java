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
import de.dhbw.fireinventory.application.vehicle.VehicleApplicationService;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.plugins.gui.vaadin.MainLayout;
import de.dhbw.plugins.gui.vaadin.tabs.EquipmentTabLayout;
import de.dhbw.plugins.gui.vaadin.tabs.LocationTabLayout;
import de.dhbw.plugins.gui.vaadin.tabs.VehicleTabLayout;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value="", layout = MainLayout.class)
@PageTitle("Overview | FireInventory")
public class Inventory extends VerticalLayout {

    EquipmentApplicationService equipmentService;
    LocationApplicationService locationService;
    VehicleApplicationService vehicleService;
    AppointmentApplicationService appointmentService;
    ItemApplicationService itemService;

    EquipmentTabLayout equipmentTabLayout;
    VehicleTabLayout vehicleTabLayout;
    LocationTabLayout locationTabLayout;

    Span equipmentBadge;
    Span vehicleBadge;

    Tab equipmentTab;
    Tab vehiclesTab;
    Tab locationTab;

    @Autowired
    public Inventory(EquipmentApplicationService equipmentService, ItemApplicationService itemService, LocationApplicationService locationService,VehicleApplicationService vehicleService, AppointmentApplicationService appointmentService) {
        this.equipmentService = equipmentService;
        this.locationService = locationService;
        this.vehicleService = vehicleService;
        this.appointmentService = appointmentService;
        this.itemService = itemService;

        setSizeFull();
        setHeightFull();

        equipmentTab = new Tab(new Span("Equipments"));
        vehiclesTab = new Tab(new Span("Vehicles"));
        locationTab = new Tab(new Span("Places"));

        configureLayouts();
        equipmentBadge = createBadge(equipmentService.equipmentStatusCount(Status.KAPUTT));
        equipmentBadge.addClickListener(e -> navigateToDestroyedEquipments());
        vehicleBadge = createBadge(vehicleService.vehicleStatusCount(Status.KAPUTT));
        vehicleBadge.addClickListener(e -> navigateToDestroyedVehicles());
        updateBadge();

        Tabs tabs = new Tabs(equipmentTab, vehiclesTab, locationTab);
        tabs.addSelectedChangeListener(
                event -> setContent(event.getSelectedTab()));
        add(tabs, equipmentTabLayout);
    }

    private void configureLayouts() {
        equipmentTabLayout = new EquipmentTabLayout(equipmentService, locationService, itemService);
        equipmentTabLayout.addListener(EquipmentTabLayout.GridUpdatedEvent.class, e -> updateBadge());

        vehicleTabLayout = new VehicleTabLayout(locationService, itemService, vehicleService, appointmentService);
        equipmentTabLayout.addListener(EquipmentTabLayout.GridUpdatedEvent.class, e -> updateBadge());

        locationTabLayout = new LocationTabLayout(locationService);
    }

    private void navigateToDestroyedEquipments() {
        equipmentTab.setSelected(true);
        equipmentTabLayout.setStatusComboBox(Status.KAPUTT);
    }

    private void navigateToDestroyedVehicles() {
        vehiclesTab.setSelected(true);
        vehicleTabLayout.setStatusComboBox(Status.KAPUTT);
    }

    private void updateBadge() {
        int destroyedEquipmentCount = equipmentService.equipmentStatusCount(Status.KAPUTT);
        int destroyedVehicleCount = vehicleService.vehicleStatusCount(Status.KAPUTT);

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
        remove(equipmentTabLayout, locationTabLayout, vehicleTabLayout);

        if (tab.equals(equipmentTab)) {
            add(equipmentTabLayout);
        } else if (tab.equals(vehiclesTab)) {
            add(vehicleTabLayout);
        } else {
            add(locationTabLayout);
        }
    }
}

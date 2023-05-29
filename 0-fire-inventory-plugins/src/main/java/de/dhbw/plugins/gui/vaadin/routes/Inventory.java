package de.dhbw.plugins.gui.vaadin.routes;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.dhbw.fireinventory.adapter.application.appointment.AppointmentAppAdapter;
import de.dhbw.fireinventory.adapter.application.equipment.EquipmentAppAdapter;
import de.dhbw.fireinventory.adapter.application.item.ItemAppAdapter;
import de.dhbw.fireinventory.adapter.application.location.LocationAppAdapter;
import de.dhbw.fireinventory.adapter.application.vehicle.VehicleAppAdapter;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.plugins.gui.vaadin.MainLayout;
import de.dhbw.plugins.gui.vaadin.tabs.EquipmentTabLayout;
import de.dhbw.plugins.gui.vaadin.tabs.LocationTabLayout;
import de.dhbw.plugins.gui.vaadin.tabs.VehicleTabLayout;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value="", layout = MainLayout.class)
@PageTitle("Inventar | FireInventory")
public class Inventory extends VerticalLayout {

    EquipmentAppAdapter equipmentAppAdapter;
    LocationAppAdapter locationAppAdapter;
    VehicleAppAdapter vehicleAppAdapter;
    AppointmentAppAdapter appointmentAppAdapter;
    ItemAppAdapter itemAppAdapter;

    EquipmentTabLayout equipmentTabLayout;
    VehicleTabLayout vehicleTabLayout;
    LocationTabLayout locationTabLayout;

    Span equipmentBadge;
    Span vehicleBadge;

    Tab equipmentTab;
    Tab vehiclesTab;
    Tab locationTab;

    @Autowired
    public Inventory(EquipmentAppAdapter equipmentAppAdapter, ItemAppAdapter itemAppAdapter, LocationAppAdapter locationAppAdapter, VehicleAppAdapter vehicleAppAdapter, AppointmentAppAdapter appointmentAppAdapter) {
        this.equipmentAppAdapter = equipmentAppAdapter;
        this.locationAppAdapter = locationAppAdapter;
        this.vehicleAppAdapter = vehicleAppAdapter;
        this.appointmentAppAdapter = appointmentAppAdapter;
        this.itemAppAdapter = itemAppAdapter;

        setSizeFull();
        setHeightFull();

        equipmentTab = new Tab(new Span("Geräte"));
        vehiclesTab = new Tab(new Span("Fahrzeuge"));
        locationTab = new Tab(new Span("Räumlichkeiten"));

        configureLayouts();
        equipmentBadge = createBadge(equipmentAppAdapter.equipmentStatusCount(Status.KAPUTT));
        equipmentBadge.addClickListener(e -> navigateToDestroyedEquipments());
        vehicleBadge = createBadge(vehicleAppAdapter.vehicleStatusCount(Status.KAPUTT));
        vehicleBadge.addClickListener(e -> navigateToDestroyedVehicles());
        updateBadge();

        Tabs tabs = new Tabs(equipmentTab, vehiclesTab, locationTab);
        tabs.addSelectedChangeListener(
                event -> setContent(event.getSelectedTab()));
        add(tabs, equipmentTabLayout);
    }

    private void configureLayouts() {
        equipmentTabLayout = new EquipmentTabLayout(equipmentAppAdapter, locationAppAdapter, itemAppAdapter);
        equipmentTabLayout.addListener(EquipmentTabLayout.GridUpdatedEvent.class, e -> updateBadge());

        vehicleTabLayout = new VehicleTabLayout(locationAppAdapter, itemAppAdapter, vehicleAppAdapter, appointmentAppAdapter);
        equipmentTabLayout.addListener(EquipmentTabLayout.GridUpdatedEvent.class, e -> updateBadge());

        locationTabLayout = new LocationTabLayout(locationAppAdapter);
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
        int destroyedEquipmentCount = equipmentAppAdapter.equipmentStatusCount(Status.KAPUTT);
        int destroyedVehicleCount = vehicleAppAdapter.vehicleStatusCount(Status.KAPUTT);

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

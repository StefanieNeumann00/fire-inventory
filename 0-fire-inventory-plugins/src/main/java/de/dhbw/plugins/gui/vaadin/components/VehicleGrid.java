package de.dhbw.plugins.gui.vaadin.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.application.vehicle.VehicleApplicationService;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

public class VehicleGrid extends AbstractGrid<Vehicle> {
    VehicleApplicationService vehicleService;

    public VehicleGrid(VehicleApplicationService vehicleService) {
        super(Vehicle.class);
        this.vehicleService = vehicleService;
        updateList(null, null, null);
    }

    protected void configureGridColumns() {
        this.getColumns().forEach(col -> col.setAutoWidth(true));
        this.addColumn(Vehicle::getDesignation).setHeader("Bezeichnung");
        this.addColumn(v -> v.getPlace().getDesignation()).setHeader("Abstellort");
        this.addColumn(v -> v.getStatus().getDesignation()).setHeader("Status");
        this.addColumn(
                new ComponentRenderer<>(Button::new, (button, vehicle) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(event -> fireEvent(new VehicleGrid.AddAppointmentEvent(this, vehicle)));
                    button.setIcon(new Icon(VaadinIcon.CALENDAR));
                })).setHeader("Manage");
        this.asSingleSelect().addValueChangeListener(event ->
                fireEvent(new VehicleGrid.EditVehicleEvent(this, event.getValue())));
    }

    public void updateList(Place place, Status status, String filterText) {
        this.setItems(vehicleService.findAllVehiclesBy(place, status, filterText));
    }

    public static abstract class VehicleGridEvent extends ComponentEvent<VehicleGrid> {
        private Vehicle vehicle;

        protected VehicleGridEvent(VehicleGrid source, Vehicle vehicle) {
            super(source, false);
            this.vehicle = vehicle;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }
    }

    public static class AddAppointmentEvent extends VehicleGrid.VehicleGridEvent {
        AddAppointmentEvent(VehicleGrid source, Vehicle vehicle) {
            super(source, vehicle);
        }
    }

    public static class EditVehicleEvent extends VehicleGrid.VehicleGridEvent {
        EditVehicleEvent(VehicleGrid source, Vehicle vehicle) {
            super(source, vehicle);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

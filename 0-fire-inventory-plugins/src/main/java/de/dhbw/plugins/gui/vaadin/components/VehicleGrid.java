package de.dhbw.plugins.gui.vaadin.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.application.vehicle.VehicleApplicationService;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.location.Location;
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
        this.addColumn(v -> v.getLocation().getDesignation()).setHeader("Abstellort");
        this.addColumn(v -> v.getStatus().getDesignation()).setHeader("Status");
        this.addComponentColumn(v -> createManageIconBar(v)).setHeader("Manage");
        this.asSingleSelect().addValueChangeListener(event ->
                fireEvent(new VehicleGrid.EditVehicleEvent(this, event.getValue())));
    }

    public Component createManageIconBar(Vehicle vehicle){
        HorizontalLayout manageBarLayout = new HorizontalLayout();

        Button addAppointmentButton = new Button();
        Button conditionChangedButton = new Button();

        addAppointmentButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_TERTIARY);
        conditionChangedButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_TERTIARY);
        manageBarLayout.add(addAppointmentButton);
        manageBarLayout.add(conditionChangedButton);

        addAppointmentButton.addClickListener(event -> fireEvent(new VehicleGrid.AddAppointmentEvent(this, vehicle)));
        conditionChangedButton.addClickListener(event -> conditionChangedButtonClicked(conditionChangedButton, vehicle));

        addAppointmentButton.setIcon(new Icon(VaadinIcon.CALENDAR));
        conditionChangedButton.setIcon(new Icon(VaadinIcon.MINUS_CIRCLE));

        return manageBarLayout;
    }

    private void conditionChangedButtonClicked(Button button, Vehicle vehicle) {
        if (vehicle.getCondition() == Condition.FUNKTIONSFÄHIG){
            button.setIcon(new Icon(VaadinIcon.COG));
        }
        else if (vehicle.getCondition() == Condition.NICHT_FUNKTIONSFÄHIG){
            button.setIcon(new Icon(VaadinIcon.CHECK));
        }
        else { button.setIcon(new Icon(VaadinIcon.MINUS_CIRCLE));}
        fireEvent(new VehicleGrid.VehicleConditionChangedEvent(this, vehicle));
    }

    public void updateList(Location location, Status status, String filterText) {
        this.setItems(vehicleService.findAllVehiclesBy(location, status, filterText));
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

    public static class VehicleConditionChangedEvent extends VehicleGrid.VehicleGridEvent {
        VehicleConditionChangedEvent(VehicleGrid source, Vehicle vehicle) {
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

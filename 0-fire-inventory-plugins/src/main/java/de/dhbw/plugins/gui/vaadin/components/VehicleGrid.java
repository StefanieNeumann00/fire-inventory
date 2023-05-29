package de.dhbw.plugins.gui.vaadin.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.BackEndDataProvider;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.adapter.application.vehicle.VehicleAppAdapter;
import de.dhbw.fireinventory.application.domain.service.equipment.EquipmentResource;
import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.application.domain.service.vehicle.VehicleResource;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.status.Status;

import java.util.ArrayList;
import java.util.List;

public class VehicleGrid extends AbstractGrid<VehicleResource> {
    VehicleAppAdapter vehicleAppAdapter;

    public VehicleGrid(VehicleAppAdapter vehicleAppAdapter) {
        super(VehicleResource.class);
        this.vehicleAppAdapter = vehicleAppAdapter;
        updateList(null, null, null);
    }

    protected void configureGridColumns() {
        this.getColumns().forEach(col -> col.setAutoWidth(true));
        this.addColumn(VehicleResource::getDesignation).setHeader("Bezeichnung");
        this.addColumn(v -> v.getLocationResource().getDesignation()).setHeader("Abstellort");
        this.addColumn(v -> v.getStatus().getDesignation()).setHeader("Status");
        this.addComponentColumn(v -> createManageIconBar(v)).setHeader("Manage");
        this.asSingleSelect().addValueChangeListener(event ->
                fireEvent(new VehicleGrid.EditVehicleEvent(this, event.getValue())));
    }

    public Component createManageIconBar(VehicleResource vehicleResource){
        HorizontalLayout manageBarLayout = new HorizontalLayout();

        Button addAppointmentButton = new Button();
        Button conditionChangedButton = new Button();

        addAppointmentButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_TERTIARY);
        conditionChangedButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_TERTIARY);
        manageBarLayout.add(addAppointmentButton);
        manageBarLayout.add(conditionChangedButton);

        addAppointmentButton.addClickListener(event -> fireEvent(new VehicleGrid.AddAppointmentEvent(this, vehicleResource)));
        conditionChangedButton.addClickListener(event -> conditionChangedButtonClicked(conditionChangedButton, vehicleResource));

        addAppointmentButton.setIcon(new Icon(VaadinIcon.CALENDAR));
        conditionChangedButton.setIcon(new Icon(VaadinIcon.MINUS_CIRCLE));

        return manageBarLayout;
    }

    private void conditionChangedButtonClicked(Button button, VehicleResource vehicleResource) {
        if (vehicleResource.getCondition() == Condition.FUNKTIONSFÄHIG){
            button.setIcon(new Icon(VaadinIcon.COG));
        }
        else if (vehicleResource.getCondition() == Condition.NICHT_FUNKTIONSFÄHIG){
            button.setIcon(new Icon(VaadinIcon.CHECK));
        }
        else { button.setIcon(new Icon(VaadinIcon.MINUS_CIRCLE));}
        fireEvent(new VehicleGrid.VehicleConditionChangedEvent(this, vehicleResource));
    }

    public void updateList(LocationResource locationResource, Status status, String filterText) {
        List<ItemResource> itemResources = vehicleAppAdapter.findAllVehiclesBy(locationResource, status, filterText);
        List<VehicleResource> vehicleResources = new ArrayList<>();
        for (ItemResource itemResource : itemResources) {
            if (itemResource instanceof VehicleResource) {
                vehicleResources.add((VehicleResource) itemResource);
            }
        }
        this.setItems(vehicleResources);
    }

    public static abstract class VehicleGridEvent extends ComponentEvent<VehicleGrid> {
        private VehicleResource vehicleResource;

        protected VehicleGridEvent(VehicleGrid source, VehicleResource vehicleResource) {
            super(source, false);
            this.vehicleResource = vehicleResource;
        }

        public VehicleResource getVehicleResource() {
            return vehicleResource;
        }
    }

    public static class AddAppointmentEvent extends VehicleGrid.VehicleGridEvent {
        AddAppointmentEvent(VehicleGrid source, VehicleResource vehicleResource) {
            super(source, vehicleResource);
        }
    }

    public static class VehicleConditionChangedEvent extends VehicleGrid.VehicleGridEvent {
        VehicleConditionChangedEvent(VehicleGrid source, VehicleResource vehicleResource) {
            super(source, vehicleResource);
        }
    }

    public static class EditVehicleEvent extends VehicleGrid.VehicleGridEvent {
        EditVehicleEvent(VehicleGrid source, VehicleResource vehicleResource) {
            super(source, vehicleResource);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

package de.dhbw.plugins.gui.vaadin.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.adapter.application.location.LocationAppAdapter;
import de.dhbw.fireinventory.application.domain.service.location.LocationApplicationService;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.domain.location.Location;

public class LocationGrid extends AbstractGrid<LocationResource>{
    LocationAppAdapter locationAppAdapter;

    public LocationGrid(LocationAppAdapter locationAppAdapter) {
        super(LocationResource.class);
        this.locationAppAdapter = locationAppAdapter;
        updateList(null);
    }

    protected void configureGridColumns() {
        this.getColumns().forEach(col -> col.setAutoWidth(true));
        this.addColumn(LocationResource::getDesignation).setHeader("Bezeichnung");
        this.asSingleSelect().addValueChangeListener(event ->
                fireEvent(new LocationGrid.EditLocationEvent(this, event.getValue())));
    }

    public void updateList(String filterText) {
        this.setItems(locationAppAdapter.findAllPlaces(filterText));
    }

    public static abstract class LocationGridEvent extends ComponentEvent<LocationGrid> {
        private LocationResource locationResource;

        protected LocationGridEvent(LocationGrid source, LocationResource locationResource) {
            super(source, false);
            this.locationResource = locationResource;
        }

        public LocationResource getLocationResource() {
            return locationResource;
        }
    }

    public static class EditLocationEvent extends LocationGridEvent {
        EditLocationEvent(LocationGrid source, LocationResource locationResource) {
            super(source, locationResource);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}

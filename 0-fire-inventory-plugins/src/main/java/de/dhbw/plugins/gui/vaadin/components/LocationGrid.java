package de.dhbw.plugins.gui.vaadin.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.application.location.LocationApplicationService;
import de.dhbw.fireinventory.domain.location.Location;

public class LocationGrid extends AbstractGrid<Location>{
    LocationApplicationService locationService;

    public LocationGrid(LocationApplicationService locationService) {
        super(Location.class);
        this.locationService = locationService;
        updateList(null);
    }

    protected void configureGridColumns() {
        this.getColumns().forEach(col -> col.setAutoWidth(true));
        this.addColumn(Location::getDesignation).setHeader("Bezeichnung");
        this.asSingleSelect().addValueChangeListener(event ->
                fireEvent(new LocationGrid.EditLocationEvent(this, event.getValue())));
    }

    public void updateList(String filterText) {
        this.setItems(locationService.findAllPlaces(filterText));
    }

    public static abstract class LocationGridEvent extends ComponentEvent<LocationGrid> {
        private Location location;

        protected LocationGridEvent(LocationGrid source, Location location) {
            super(source, false);
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }
    }

    public static class EditLocationEvent extends LocationGridEvent {
        EditLocationEvent(LocationGrid source, Location location) {
            super(source, location);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}

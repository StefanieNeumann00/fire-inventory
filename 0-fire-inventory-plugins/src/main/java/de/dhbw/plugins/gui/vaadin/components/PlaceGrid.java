package de.dhbw.plugins.gui.vaadin.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.application.place.PlaceApplicationService;
import de.dhbw.fireinventory.domain.place.Place;

public class PlaceGrid extends AbstractGrid<Place>{
    PlaceApplicationService placeService;

    public PlaceGrid(PlaceApplicationService placeService) {
        super(Place.class);
        this.placeService = placeService;
        updateList(null);
    }

    protected void configureGridColumns() {
        this.getColumns().forEach(col -> col.setAutoWidth(true));
        this.addColumn(Place::getDesignation).setHeader("Bezeichnung");
        this.asSingleSelect().addValueChangeListener(event ->
                fireEvent(new PlaceGrid.EditPlaceEvent(this, event.getValue())));
    }

    public void updateList(String filterText) {
        this.setItems(placeService.findAllBy(filterText));
    }

    public static abstract class PlaceGridEvent extends ComponentEvent<PlaceGrid> {
        private Place place;

        protected PlaceGridEvent(PlaceGrid source, Place place) {
            super(source, false);
            this.place = place;
        }

        public Place getPlace() {
            return place;
        }
    }

    public static class EditPlaceEvent extends PlaceGrid.PlaceGridEvent {
        EditPlaceEvent(PlaceGrid source, Place place) {
            super(source, place);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}

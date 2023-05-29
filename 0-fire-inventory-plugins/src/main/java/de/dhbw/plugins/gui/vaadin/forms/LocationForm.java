package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.application.domain.service.internalPlace.InternalPlaceResource;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.domain.location.InternalPlace;
import de.dhbw.fireinventory.domain.location.Location;

public class LocationForm extends FormLayout {
    TextField designation = new TextField("Bezeichnung");
    Button save = new Button("Speichern");
    Button delete = new Button("Löschen");
    Button close = new Button("Abbrechen");
    Binder<LocationResource> binder = new BeanValidationBinder<>(LocationResource.class);
    private LocationResource locationResource = new InternalPlaceResource();

    public LocationForm() {
        this.add(designation,createButtonsLayout());
        binder.bindInstanceFields(this);
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
    }

    public void setLocation(LocationResource locationResource) {
        this.locationResource = locationResource;
        binder.readBean(locationResource);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, locationResource)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, delete,  close);
    }

    public void validateAndSave() {
        try {
            binder.writeBean(locationResource);
            fireEvent(new LocationForm.SaveEvent(this, locationResource));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class LocationFormEvent extends ComponentEvent<LocationForm> {
        private LocationResource locationResource;

        protected LocationFormEvent(LocationForm source, LocationResource locationResource) {
            super(source, false);
            this.locationResource = locationResource;
        }

        public LocationResource getLocationResource() {
            return locationResource;
        }
    }

    public static class SaveEvent extends LocationFormEvent {
        SaveEvent(LocationForm source, LocationResource locationResource) {
            super(source, locationResource);
        }
    }

    public static class DeleteEvent extends LocationFormEvent {
        DeleteEvent(LocationForm source, LocationResource locationResource) {
            super(source, locationResource);
        }

    }

    public static class CloseEvent extends LocationFormEvent {
        CloseEvent(LocationForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

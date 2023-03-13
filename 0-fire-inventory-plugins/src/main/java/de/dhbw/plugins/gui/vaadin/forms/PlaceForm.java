package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.application.place.PlaceApplicationService;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.place.Place;

public class PlaceForm extends Dialog implements FormDialog {
    TextField designation = new TextField("Designation");
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<Place> binder = new BeanValidationBinder<>(Place.class);
    private Place place = new Place();

    public PlaceForm() {
        this.setResizable(true);
        this.setDraggable(true);

        this.setHeaderTitle("Add Place");

        this.add(designation,createButtonsLayout());
        binder.bindInstanceFields(this);
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
    }

    public void setPlace(Place place) {
        this.place = place;
        binder.readBean(place);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> close());

        return new HorizontalLayout(save, close);
    }

    public void validateAndSave() {
        try {
            binder.writeBean(place);
            fireEvent(new PlaceForm.SaveEvent(this, place));
            this.close();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class PlaceFormEvent extends ComponentEvent<PlaceForm> {
        private Place place;

        protected PlaceFormEvent(PlaceForm source, Place place) {
            super(source, false);
            this.place = place;
        }

        public Place getPlace() {
            return place;
        }
    }

    public static class SaveEvent extends PlaceFormEvent {
        SaveEvent(PlaceForm source, Place place) {
            super(source, place);
        }
    }

    public static class DeleteEvent extends PlaceFormEvent {
        DeleteEvent(PlaceForm source, Place place) {
            super(source, place);
        }

    }

    public static class CloseEvent extends PlaceFormEvent {
        CloseEvent(PlaceForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

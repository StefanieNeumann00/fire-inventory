package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import de.dhbw.fireinventory.application.place.PlaceApplicationService;
import de.dhbw.fireinventory.domain.place.Place;

public class PlaceForm extends Dialog implements FormDialog {
    PlaceApplicationService service;
    TextField designation = new TextField("Designation");
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<Place> binder = new BeanValidationBinder<>(Place.class);
    private Place place = new Place();

    public PlaceForm(PlaceApplicationService service) {
        this.service = service;
        this.setResizable(true);
        this.setDraggable(true);

        add(designation,createButtonsLayout());
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
        close.addClickListener(event -> closeDialog());

        return new HorizontalLayout(save, close);
    }

    public void validateAndSave() {
        try {
            binder.writeBean(place);
            service.savePlace(place);
            this.close();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void closeDialog() {
        this.close();
    }
}

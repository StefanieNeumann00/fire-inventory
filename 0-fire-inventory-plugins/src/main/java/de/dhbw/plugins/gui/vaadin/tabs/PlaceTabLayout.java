package de.dhbw.plugins.gui.vaadin.tabs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import de.dhbw.fireinventory.application.location.LocationApplicationService;
import de.dhbw.fireinventory.application.place.PlaceApplicationService;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.plugins.gui.vaadin.components.ErrorMessage;
import de.dhbw.plugins.gui.vaadin.components.PlaceGrid;
import de.dhbw.plugins.gui.vaadin.forms.PlaceForm;
import org.springframework.dao.DataIntegrityViolationException;

public class PlaceTabLayout extends AbstractTabLayout{

    PlaceGrid grid;
    TextField filterText = new TextField("Bezeichnung");
    HorizontalLayout filterLayout;
    PlaceForm placeForm;
    LocationApplicationService locationService;
    PlaceApplicationService placeService;

    public PlaceTabLayout(LocationApplicationService locationService, PlaceApplicationService placeService)
    {
        super();

        this.locationService = locationService;
        this.placeService = placeService;

        configureGrid();
        configurePlaceForm();

        add(getToolbar(), getContent());
        closePlaceEditor();
    }

    private void configurePlaceForm() {
        placeForm = new PlaceForm();
        placeForm.setWidth("25em");
        placeForm.addListener(PlaceForm.SaveEvent.class, this::savePlace);
        placeForm.addListener(PlaceForm.DeleteEvent.class, this::deletePlace);
        placeForm.addListener(PlaceForm.CloseEvent.class, e -> closePlaceEditor());
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, placeForm);

        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, placeForm);

        content.addClassNames("content");
        content.setSizeFull();
        content.setHeightFull();

        return content;
    }

    protected void configureGrid() {
        grid = new PlaceGrid(placeService);
        grid.addListener(PlaceGrid.EditPlaceEvent.class, e -> editPlace(e.getPlace()));
    }

    private void editPlace(Place place) {
        if (place == null) {
            closePlaceEditor();
        } else {
            placeForm.setPlace(place);
            placeForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closePlaceEditor() {
        placeForm.setPlace(null);
        placeForm.setVisible(false);
        removeClassName("editing");
    }

    private void addPlace() {
        grid.clearGridSelection();
        editPlace(new Place());
    }

    private void updateList() {
        grid.updateList(filterText.getValue());
    }

    private void savePlace(PlaceForm.SaveEvent event) {
        Place place = event.getPlace();
        placeService.savePlace(place);
        updateList();
        closePlaceEditor();
    }

    private void deletePlace(PlaceForm.DeleteEvent event) {
        try {
            locationService.deleteLocation(event.getPlace());
            placeService.deletePlace(event.getPlace());
            updateList();
            closePlaceEditor();
        }
        catch (DataIntegrityViolationException exception) {
            new ErrorMessage("You cannot delete this place since there are still equipments attached.");
        }

    }

    private VerticalLayout getToolbar() {
        filterText.setPlaceholder("...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Icon filterIcon = new Icon(VaadinIcon.FILTER);
        filterIcon.addClickListener(e -> changeFilterVisibility());

        Button addPlaceButton = new Button("Add Place");

        addPlaceButton.addClickListener(event -> addPlace());

        HorizontalLayout buttonsLayout = new HorizontalLayout(filterIcon, addPlaceButton);
        buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        filterLayout = new HorizontalLayout(filterText);
        filterLayout.setVisible(false);

        VerticalLayout toolbar = new VerticalLayout(buttonsLayout, filterLayout);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void changeFilterVisibility() {
        if(filterLayout.isVisible()) { filterLayout.setVisible(false); }
        else { filterLayout.setVisible(true); }
    }
}

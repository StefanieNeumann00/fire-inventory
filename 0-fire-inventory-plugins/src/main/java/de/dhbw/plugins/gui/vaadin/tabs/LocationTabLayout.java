package de.dhbw.plugins.gui.vaadin.tabs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import de.dhbw.fireinventory.adapter.application.location.LocationAppAdapter;
import de.dhbw.fireinventory.application.domain.service.internalPlace.InternalPlaceResource;
import de.dhbw.fireinventory.application.domain.service.location.LocationApplicationService;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.domain.location.InternalPlace;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.plugins.gui.vaadin.components.ErrorMessage;
import de.dhbw.plugins.gui.vaadin.components.LocationGrid;
import de.dhbw.plugins.gui.vaadin.forms.LocationForm;
import org.springframework.dao.DataIntegrityViolationException;

public class LocationTabLayout extends AbstractTabLayout{

    LocationGrid grid;
    TextField filterText = new TextField("Bezeichnung");
    HorizontalLayout filterLayout;
    LocationForm locationForm;
    LocationAppAdapter locationAppAdapter;

    public LocationTabLayout(LocationAppAdapter locationAppAdapter)
    {
        super();

        this.locationAppAdapter = locationAppAdapter;

        configureGrid();
        configurePlaceForm();

        add(getToolbar(), getContent());
        closePlaceEditor();
        updateList();
    }

    private void configurePlaceForm() {
        locationForm = new LocationForm();
        locationForm.setWidth("25em");
        locationForm.addListener(LocationForm.SaveEvent.class, this::savePlace);
        locationForm.addListener(LocationForm.DeleteEvent.class, this::deletePlace);
        locationForm.addListener(LocationForm.CloseEvent.class, e -> closePlaceEditor());
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, locationForm);

        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, locationForm);

        content.addClassNames("content");
        content.setSizeFull();
        content.setHeightFull();

        return content;
    }

    protected void configureGrid() {
        grid = new LocationGrid(locationAppAdapter);
        grid.addListener(LocationGrid.EditLocationEvent.class, e -> editLocation(e.getLocationResource()));
    }

    private void editLocation(LocationResource locationResource) {
        if (locationResource == null) {
            closePlaceEditor();
        } else {
            locationForm.setLocation(locationResource);
            locationForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closePlaceEditor() {
        locationForm.setLocation(null);
        locationForm.setVisible(false);
        removeClassName("editing");
    }

    private void addInternalLocation() {
        grid.clearGridSelection();
        editLocation(new InternalPlaceResource());
    }

    private void updateList() {
        grid.updateList(filterText.getValue());
    }

    private void savePlace(LocationForm.SaveEvent event) {
        LocationResource locationResource = event.getLocationResource();
        locationAppAdapter.saveLocation(locationResource);
        updateList();
        closePlaceEditor();
    }

    private void deletePlace(LocationForm.DeleteEvent event) {
        try {
            locationAppAdapter.deleteLocation(event.getLocationResource());
            locationAppAdapter.deleteLocation(event.getLocationResource());
            updateList();
            closePlaceEditor();
        }
        catch (DataIntegrityViolationException exception) {
            new ErrorMessage("Diese Räumlichkeit kann nicht gelöscht werden, da es noch verknüpfte Gegenstände gibt.");
        }

    }

    private VerticalLayout getToolbar() {
        filterText.setPlaceholder("...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Icon filterIcon = new Icon(VaadinIcon.FILTER);
        filterIcon.addClickListener(e -> changeFilterVisibility());

        Button addPlaceButton = new Button("Interne Räumlichkeit hinzufügen");

        addPlaceButton.addClickListener(event -> addInternalLocation());

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

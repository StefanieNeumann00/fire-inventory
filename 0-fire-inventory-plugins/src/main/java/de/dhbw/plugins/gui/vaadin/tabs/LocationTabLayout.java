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
    LocationApplicationService locationService;

    public LocationTabLayout(LocationApplicationService locationService)
    {
        super();

        this.locationService = locationService;

        configureGrid();
        configurePlaceForm();

        add(getToolbar(), getContent());
        closePlaceEditor();
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
        grid = new LocationGrid(locationService);
        grid.addListener(LocationGrid.EditLocationEvent.class, e -> editLocation(e.getLocation()));
    }

    private void editLocation(Location location) {
        if (location == null) {
            closePlaceEditor();
        } else {
            locationForm.setLocation(location);
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
        editLocation(new InternalPlace());
    }

    private void updateList() {
        grid.updateList(filterText.getValue());
    }

    private void savePlace(LocationForm.SaveEvent event) {
        Location location = event.getLocation();
        locationService.saveLocation(location);
        updateList();
        closePlaceEditor();
    }

    private void deletePlace(LocationForm.DeleteEvent event) {
        try {
            locationService.deleteLocation(event.getLocation());
            locationService.deleteLocation(event.getLocation());
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

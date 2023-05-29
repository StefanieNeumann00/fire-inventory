package de.dhbw.plugins.gui.vaadin.tabs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import de.dhbw.fireinventory.adapter.application.appointment.AppointmentAppAdapter;
import de.dhbw.fireinventory.adapter.application.item.ItemAppAdapter;
import de.dhbw.fireinventory.adapter.application.location.LocationAppAdapter;
import de.dhbw.fireinventory.adapter.application.vehicle.VehicleAppAdapter;
import de.dhbw.fireinventory.application.domain.service.appointment.AppointmentResource;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.application.domain.service.vehicle.VehicleResource;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.plugins.gui.vaadin.components.ErrorMessage;
import de.dhbw.plugins.gui.vaadin.components.VehicleGrid;
import de.dhbw.plugins.gui.vaadin.forms.*;
import de.dhbw.plugins.gui.vaadin.routes.Calendar;
import org.springframework.dao.DataIntegrityViolationException;

public class VehicleTabLayout extends AbstractTabLayout{

    VehicleGrid grid;
    TextField filterText = new TextField("Bezeichnung");
    HorizontalLayout filterLayout;
    VehicleForm vehicleForm;
    LocationAppAdapter locationAppAdapter;
    VehicleAppAdapter vehicleAppAdapter;
    AppointmentAppAdapter appointmentAppAdapter;
    ItemAppAdapter itemAppAdapter;
    ComboBox<LocationResource> locationComboBox;
    ComboBox<Status> statusComboBox;

    public VehicleTabLayout(LocationAppAdapter locationAppAdapter, ItemAppAdapter itemAppAdapter, VehicleAppAdapter vehicleAppAdapter, AppointmentAppAdapter appointmentAppAdapter)
    {
        super();

        this.itemAppAdapter = itemAppAdapter;
        this.locationAppAdapter = locationAppAdapter;
        this.vehicleAppAdapter = vehicleAppAdapter;
        this.appointmentAppAdapter = appointmentAppAdapter;

        configureGrid();
        configureVehicleForm();

        add(getToolbar(), getContent());
        closeVehicleEditor();
        updateList();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, vehicleForm);

        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, vehicleForm);

        content.addClassNames("content");
        content.setSizeFull();
        content.setHeightFull();

        return content;
    }

    private void configureVehicleForm() {
        vehicleForm = new VehicleForm(locationAppAdapter.findAllPlaces(null));
        vehicleForm.setWidth("25em");
        vehicleForm.addListener(VehicleForm.SaveEvent.class, this::saveVehicle);
        vehicleForm.addListener(VehicleForm.DeleteEvent.class, this::deleteVehicle);
        vehicleForm.addListener(VehicleForm.CloseEvent.class, e -> closeVehicleEditor());
    }

    protected void configureGrid() {
        grid = new VehicleGrid(vehicleAppAdapter);
        grid.addListener(VehicleGrid.AddAppointmentEvent.class, e -> addAppointment(e.getVehicleResource()));
        grid.addListener(VehicleGrid.EditVehicleEvent.class, e -> editVehicle(e.getVehicleResource()));
        grid.addListener(VehicleGrid.VehicleConditionChangedEvent.class, e -> changeCondition(e.getVehicleResource()));
    }

    private void changeCondition(VehicleResource vehicleResource) {
        vehicleAppAdapter.changeCondition(vehicleResource);
        updateList();
    }

    private VerticalLayout getToolbar() {
        filterText.setPlaceholder("...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Icon filterIcon = new Icon(VaadinIcon.FILTER);
        filterIcon.addClickListener(e -> changeFilterVisibility());

        Button addVehicleButton = new Button("Fahrzeug hinzufügen");

        locationComboBox = new ComboBox<>("Abstellort");
        locationComboBox.setItems(locationAppAdapter.findAllPlaces(null));
        locationComboBox.setItemLabelGenerator(LocationResource::getDesignation);
        locationComboBox.setClearButtonVisible(true);
        locationComboBox.addValueChangeListener(e -> updateList());

        statusComboBox = new ComboBox<>("Status");
        statusComboBox.setItems(Status.values());
        statusComboBox.setItemLabelGenerator(Status::getDesignation);
        statusComboBox.setClearButtonVisible(true);
        statusComboBox.addValueChangeListener(e -> updateList());

        addVehicleButton.addClickListener(event -> addVehicle());

        HorizontalLayout buttonsLayout = new HorizontalLayout(filterIcon, addVehicleButton);
        buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        filterLayout = new HorizontalLayout(locationComboBox, statusComboBox, filterText);
        filterLayout.setVisible(false);

        VerticalLayout toolbar = new VerticalLayout(buttonsLayout, filterLayout);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addAppointment(VehicleResource vehicleResource) {
        AppointmentResource appointmentResource = new AppointmentResource();
        appointmentResource.setItemResource(vehicleResource);
        this.getUI().ifPresent(ui -> ui.navigate(Calendar.class).ifPresent(calendar -> calendar.editAppointment(appointmentResource)));
    }

    private void changeFilterVisibility() {
        if(filterLayout.isVisible()) { filterLayout.setVisible(false); }
        else { filterLayout.setVisible(true); }
    }

    public void setStatusComboBox(Status status) { statusComboBox.setValue(status); }

    private void deleteVehicle(VehicleForm.DeleteEvent event) {
        try {
            vehicleAppAdapter.deleteVehicle(event.getVehicleResource());
            updateList();
            closeVehicleEditor();
            fireEvent(new GridUpdatedEvent(this));
        }
        catch (DataIntegrityViolationException exception) {
            new ErrorMessage("Dieses Fahrzeug kann nicht gelöscht werden, da es noch verknüpfte Gerätschaften gibt.");
        }

    }

    public void editVehicle(VehicleResource vehicleResource) {
        if (vehicleResource == null) {
            closeVehicleEditor();
        } else {
            vehicleForm.setVehicle(vehicleResource);
            vehicleForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeVehicleEditor() {
        vehicleForm.setVehicle(null);
        vehicleForm.setVisible(false);
        removeClassName("editing");
    }

    private void addVehicle() {
        grid.clearGridSelection();
        editVehicle(new VehicleResource());
    }

    private void updateList() {
        grid.updateList(locationComboBox.getValue(), statusComboBox.getValue(), filterText.getValue());
    }

    private void saveVehicle(VehicleForm.SaveEvent event) {
        VehicleResource vehicleResource = event.getVehicleResource();
        Condition conditionValue = vehicleForm.getConditionRadioButtonValue();
        vehicleAppAdapter.saveVehicle(vehicleResource, conditionValue);
        updateList();
        closeVehicleEditor();
        fireEvent(new GridUpdatedEvent(this));
    }

}

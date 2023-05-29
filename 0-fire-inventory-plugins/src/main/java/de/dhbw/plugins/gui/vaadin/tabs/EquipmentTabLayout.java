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
import de.dhbw.fireinventory.adapter.application.equipment.EquipmentAppAdapter;
import de.dhbw.fireinventory.adapter.application.item.ItemAppAdapter;
import de.dhbw.fireinventory.adapter.application.location.LocationAppAdapter;
import de.dhbw.fireinventory.application.domain.service.appointment.AppointmentResource;
import de.dhbw.fireinventory.application.domain.service.equipment.EquipmentResource;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.plugins.gui.vaadin.components.EquipmentGrid;
import de.dhbw.plugins.gui.vaadin.components.ErrorMessage;
import de.dhbw.plugins.gui.vaadin.forms.*;
import de.dhbw.plugins.gui.vaadin.routes.Calendar;
import org.springframework.dao.DataIntegrityViolationException;

public class EquipmentTabLayout extends AbstractTabLayout {
    EquipmentGrid grid;
    TextField filterText = new TextField("Bezeichnung");
    HorizontalLayout filterLayout;
    EquipmentForm equipmentForm;
    EquipmentAppAdapter equipmentAppAdapter;
    LocationAppAdapter locationAppAdapter;
    ItemAppAdapter itemAppAdapter;
    ComboBox<LocationResource> locationComboBox;
    ComboBox<Status> statusComboBox;

    public EquipmentTabLayout(EquipmentAppAdapter equipmentAppAdapter, LocationAppAdapter locationAppAdapter, ItemAppAdapter itemAppAdapter)
    {
        super();

        this.equipmentAppAdapter = equipmentAppAdapter;
        this.locationAppAdapter = locationAppAdapter;
        this.itemAppAdapter = itemAppAdapter;

        configureGrid();
        configureEquipmentForm();

        add(getToolbar(), getContent());
        closeEquipmentEditor();
        updateList();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, equipmentForm);

        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, equipmentForm);

        content.addClassNames("content");
        content.setSizeFull();
        content.setHeightFull();

        return content;
    }

    private void configureEquipmentForm() {
        equipmentForm = new EquipmentForm(locationAppAdapter.findAllLocations(null));
        equipmentForm.setWidth("25em");
        equipmentForm.addListener(EquipmentForm.SaveEvent.class, this::saveEquipment);
        equipmentForm.addListener(EquipmentForm.DeleteEvent.class, this::deleteEquipment);
        equipmentForm.addListener(EquipmentForm.CloseEvent.class, e -> closeEquipmentEditor());
    }

    protected void configureGrid() {
        grid = new EquipmentGrid(equipmentAppAdapter);
        grid.addListener(EquipmentGrid.AddAppointmentEvent.class, e -> addAppointment(e.getEquipment()));
        grid.addListener(EquipmentGrid.EditEquipmentEvent.class, e -> editEquipment(e.getEquipment()));
        grid.addListener(EquipmentGrid.EquipmentConditionChangedEvent.class, e -> changeCondition(e.getEquipment()));
    }

    private void addAppointment(EquipmentResource equipmentResource) {
        AppointmentResource appointmentResource = new AppointmentResource();
        appointmentResource.setItemResource(equipmentResource);
        this.getUI().ifPresent(ui -> ui.navigate(Calendar.class).ifPresent(calendar -> calendar.editAppointment(appointmentResource)));
    }

    private void changeCondition(EquipmentResource equipmentResource) {
        equipmentAppAdapter.changeCondition(equipmentResource);
        updateList();
    }

    private VerticalLayout getToolbar() {
        filterText.setPlaceholder("...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Icon filterIcon = new Icon(VaadinIcon.FILTER);
        filterIcon.addClickListener(e -> changeFilterVisibility());

        Button addEquipmentButton = new Button("Gerät hinzufügen");

        locationComboBox = new ComboBox<>("Location");
        locationComboBox.setItems(locationAppAdapter.findAllLocations(null));
        locationComboBox.setItemLabelGenerator(LocationResource::getDesignation);
        locationComboBox.setClearButtonVisible(true);
        locationComboBox.addValueChangeListener(e -> updateList());

        statusComboBox = new ComboBox<>("Status");
        statusComboBox.setItems(Status.values());
        statusComboBox.setItemLabelGenerator(Status::getDesignation);
        statusComboBox.setClearButtonVisible(true);
        statusComboBox.addValueChangeListener(e -> updateList());

        addEquipmentButton.addClickListener(event -> addEquipment());

        HorizontalLayout buttonsLayout = new HorizontalLayout(filterIcon, addEquipmentButton);
        buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        filterLayout = new HorizontalLayout(locationComboBox, statusComboBox, filterText);
        filterLayout.setVisible(false);

        VerticalLayout toolbar = new VerticalLayout(buttonsLayout, filterLayout);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void changeFilterVisibility() {
        if(filterLayout.isVisible()) { filterLayout.setVisible(false); }
        else { filterLayout.setVisible(true); }
    }

    public void setStatusComboBox(Status status) { statusComboBox.setValue(status); }

    public void editEquipment(EquipmentResource equipmentResource) {
        if (equipmentResource == null) {
            closeEquipmentEditor();
        } else {
            equipmentForm.setEquipment(equipmentResource);
            equipmentForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEquipmentEditor() {
        equipmentForm.setEquipment(null);
        equipmentForm.setVisible(false);
        removeClassName("editing");
    }

    private void addEquipment() {
        grid.clearGridSelection();
        editEquipment(new EquipmentResource());
    }

    private void updateList() {
        grid.updateList(locationComboBox.getValue(), statusComboBox.getValue(), filterText.getValue());
    }

    private void saveEquipment(EquipmentForm.SaveEvent event) {
        EquipmentResource equipmentResource = event.getEquipmentResource();
        equipmentAppAdapter.saveEquipment(equipmentResource);
        updateList();
        closeEquipmentEditor();
        fireEvent(new GridUpdatedEvent(this));
    }

    private void deleteEquipment(EquipmentForm.DeleteEvent event) {
        try {
            EquipmentResource equipmentResource = event.getEquipmentResource();
            equipmentAppAdapter.deleteEquipment(equipmentResource);
            updateList();
            closeEquipmentEditor();
            fireEvent(new GridUpdatedEvent(this));
        }
        catch (DataIntegrityViolationException exception) {
            new ErrorMessage("You cannot delete this vehicle since there are still equipments attached.");
        }
    }
}

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
import de.dhbw.fireinventory.application.equipment.EquipmentApplicationService;
import de.dhbw.fireinventory.application.item.ItemApplicationService;
import de.dhbw.fireinventory.application.location.LocationApplicationService;
import de.dhbw.fireinventory.domain.Condition;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.plugins.gui.vaadin.components.EquipmentGrid;
import de.dhbw.plugins.gui.vaadin.components.ErrorMessage;
import de.dhbw.plugins.gui.vaadin.forms.*;
import org.springframework.dao.DataIntegrityViolationException;

public class EquipmentTabLayout extends AbstractTabLayout {
    EquipmentGrid grid;
    TextField filterText = new TextField("Bezeichnung");
    HorizontalLayout filterLayout;
    EquipmentForm equipmentForm;
    EquipmentApplicationService equipmentService;
    LocationApplicationService locationService;
    ItemApplicationService itemService;
    ComboBox<Location> locationComboBox;
    ComboBox<Status> statusComboBox;

    public EquipmentTabLayout(EquipmentApplicationService equipmentService, LocationApplicationService locationService, ItemApplicationService itemService)
    {
        super();

        this.equipmentService = equipmentService;
        this.locationService = locationService;
        this.itemService = itemService;

        configureGrid();
        configureEquipmentForm();

        add(getToolbar(), getContent());
        closeEquipmentEditor();
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
        equipmentForm = new EquipmentForm(locationService.findAllLocations(null));
        equipmentForm.setWidth("25em");
        equipmentForm.addListener(EquipmentForm.SaveEvent.class, this::saveEquipment);
        equipmentForm.addListener(EquipmentForm.DeleteEvent.class, this::deleteEquipment);
        equipmentForm.addListener(EquipmentForm.CloseEvent.class, e -> closeEquipmentEditor());
    }

    protected void configureGrid() {
        grid = new EquipmentGrid(equipmentService);
        grid.addListener(EquipmentGrid.AddAppointmentEvent.class, e -> addAppointment(e.getEquipment()));
        grid.addListener(EquipmentGrid.EditEquipmentEvent.class, e -> editEquipment(e.getEquipment()));
    }

    private void addAppointment(Equipment equipment) {}

    private VerticalLayout getToolbar() {
        filterText.setPlaceholder("...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Icon filterIcon = new Icon(VaadinIcon.FILTER);
        filterIcon.addClickListener(e -> changeFilterVisibility());

        Button addEquipmentButton = new Button("Add Equipment");

        locationComboBox = new ComboBox<>("Location");
        locationComboBox.setItems(locationService.findAllLocations(null));
        locationComboBox.setItemLabelGenerator(Location::getDesignation);
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

    private void changeFilterVisibility() {
        if(filterLayout.isVisible()) { filterLayout.setVisible(false); }
        else { filterLayout.setVisible(true); }
    }

    public void setStatusComboBox(Status status) { statusComboBox.setValue(status); }

    public void editEquipment(Equipment equipment) {
        if (equipment == null) {
            closeEquipmentEditor();
        } else {
            equipmentForm.setEquipment(equipment);
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
        editEquipment(new Equipment());
    }

    private void updateList() {
        grid.updateList(locationComboBox.getValue(), statusComboBox.getValue(), filterText.getValue());
    }

    private void saveEquipment(EquipmentForm.SaveEvent event) {
        Equipment equipment = event.getEquipment();
        Condition conditionValue = equipmentForm.getConditionRadioButtonValue();
        equipmentService.saveEquipment(equipment, conditionValue);
        updateList();
        closeEquipmentEditor();
        fireEvent(new GridUpdatedEvent(this));
    }

    private void deleteEquipment(EquipmentForm.DeleteEvent event) {
        try {
            Equipment equipment = event.getEquipment();
            equipmentService.deleteEquipment(equipment);
            updateList();
            closeEquipmentEditor();
            fireEvent(new GridUpdatedEvent(this));
        }
        catch (DataIntegrityViolationException exception) {
            new ErrorMessage("You cannot delete this vehicle since there are still equipments attached.");
        }
    }
}

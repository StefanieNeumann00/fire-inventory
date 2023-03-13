package de.dhbw.plugins.gui.vaadin.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.application.equipment.EquipmentApplicationService;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;

public class EquipmentGrid extends Grid<Equipment> {

    EquipmentApplicationService equipmentService;

    public EquipmentGrid(EquipmentApplicationService equipmentService) {
        super(Equipment.class, false);
        this.equipmentService = equipmentService;
        this.setSizeFull();
        this.setHeightFull();
        configureGrid();
        updateList(null, null, null);
    }

    private void configureGrid() {
        this.addClassName("equipment-grid");
        this.setSizeFull();
        this.setHeightFull();
        this.getColumns().forEach(col -> col.setAutoWidth(true));
        this.addColumn(Equipment::getDesignation).setHeader("Bezeichnung");
        this.addColumn(e -> e.getLocation().getDesignation()).setHeader("Ablageort");
        this.addColumn(e -> e.getStatus().getDesignation()).setHeader("Status");
        this.addColumn(
                new ComponentRenderer<>(Button::new, (button, equipment) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(event -> fireEvent(new AddAppointmentEvent(this, equipment)));
                    button.setIcon(new Icon(VaadinIcon.CALENDAR));
                })).setHeader("Manage");
        this.asSingleSelect().addValueChangeListener(event ->
                fireEvent(new EditEquipmentEvent(this, event.getValue())));
    }

    public void updateList(Location location, Status status, String filterText) {
        this.setItems(equipmentService.findAllEquipmentsBy(location, status, filterText));
    }

    public void clearGridSelection() {
        this.asSingleSelect().clear();
    }

    public static abstract class EquipmentGridEvent extends ComponentEvent<EquipmentGrid> {
        private Equipment equipment;

        protected EquipmentGridEvent(EquipmentGrid source, Equipment equipment) {
            super(source, false);
            this.equipment = equipment;
        }

        public Equipment getEquipment() {
            return equipment;
        }
    }

    public static class AddAppointmentEvent extends EquipmentGrid.EquipmentGridEvent {
        AddAppointmentEvent(EquipmentGrid source, Equipment equipment) {
            super(source, equipment);
        }
    }

    public static class EditEquipmentEvent extends EquipmentGrid.EquipmentGridEvent {
        EditEquipmentEvent(EquipmentGrid source, Equipment equipment) {
            super(source, equipment);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

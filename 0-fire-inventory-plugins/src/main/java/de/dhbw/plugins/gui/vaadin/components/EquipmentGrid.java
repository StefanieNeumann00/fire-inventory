package de.dhbw.plugins.gui.vaadin.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.BackEndDataProvider;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.adapter.application.equipment.EquipmentAppAdapter;
import de.dhbw.fireinventory.application.domain.service.equipment.EquipmentResource;
import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.status.Status;

import java.util.ArrayList;
import java.util.List;

public class EquipmentGrid extends AbstractGrid<EquipmentResource> {

    EquipmentAppAdapter equipmentAppAdapter;

    public EquipmentGrid(EquipmentAppAdapter equipmentAppAdapter) {
        super(EquipmentResource.class);
        this.equipmentAppAdapter = equipmentAppAdapter;
        updateList(null, null, null);
    }

    protected void configureGridColumns() {
        this.getColumns().forEach(col -> col.setAutoWidth(true));
        this.addColumn(EquipmentResource::getDesignation).setHeader("Bezeichnung");
        this.addColumn(e -> e.getLocationResource().getDesignation()).setHeader("Ablageort");
        this.addColumn(e -> e.getStatus().getDesignation()).setHeader("Status");
        this.addComponentColumn(e -> createManageIconBar(e)).setHeader("Manage");
        this.asSingleSelect().addValueChangeListener(event ->
                fireEvent(new EditEquipmentEvent(this, event.getValue())));
    }

    public Component createManageIconBar(EquipmentResource equipmentResource){
        HorizontalLayout manageBarLayout = new HorizontalLayout();

        Button addAppointmentButton = new Button();
        Button conditionChangedButton = new Button();

        addAppointmentButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_TERTIARY);
        conditionChangedButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_TERTIARY);

        manageBarLayout.add(addAppointmentButton);
        manageBarLayout.add(conditionChangedButton);

        addAppointmentButton.addClickListener(event -> fireEvent(new EquipmentGrid.AddAppointmentEvent(this, equipmentResource)));
        conditionChangedButton.addClickListener(event -> conditionChangedButtonClicked(conditionChangedButton, equipmentResource));

        addAppointmentButton.setIcon(new Icon(VaadinIcon.CALENDAR));
        conditionChangedButton.setIcon(new Icon(VaadinIcon.MINUS_CIRCLE));

        return manageBarLayout;
    }

    private void conditionChangedButtonClicked(Button button, EquipmentResource equipmentResource) {
        if (equipmentResource.getCondition() == Condition.FUNKTIONSFÄHIG){
            button.setIcon(new Icon(VaadinIcon.COG));
        }
        else if (equipmentResource.getCondition() == Condition.NICHT_FUNKTIONSFÄHIG){
            button.setIcon(new Icon(VaadinIcon.CHECK));
        }
        else { button.setIcon(new Icon(VaadinIcon.MINUS_CIRCLE));}
        fireEvent(new EquipmentGrid.EquipmentConditionChangedEvent(this, equipmentResource));
    }

    public void updateList(LocationResource locationResource, Status status, String filterText) {
        List<ItemResource> itemResources = equipmentAppAdapter.findAllEquipmentsBy(locationResource, status, filterText);
        List<EquipmentResource> equipmentResources = new ArrayList<>();
        for (ItemResource itemResource : itemResources) {
            if (itemResource instanceof EquipmentResource) {
                equipmentResources.add((EquipmentResource) itemResource);
            }
        }
        this.setItems(equipmentResources);
    }

    public static abstract class EquipmentGridEvent extends ComponentEvent<EquipmentGrid> {
        private EquipmentResource equipmentResource;

        protected EquipmentGridEvent(EquipmentGrid source, EquipmentResource equipmentResource) {
            super(source, false);
            this.equipmentResource = equipmentResource;
        }

        public EquipmentResource getEquipment() {
            return equipmentResource;
        }
    }

    public static class AddAppointmentEvent extends EquipmentGrid.EquipmentGridEvent {
        AddAppointmentEvent(EquipmentGrid source, EquipmentResource equipmentResource) {
            super(source, equipmentResource);
        }
    }

    public static class EquipmentConditionChangedEvent extends EquipmentGrid.EquipmentGridEvent {
        EquipmentConditionChangedEvent(EquipmentGrid source, EquipmentResource equipmentResource) {
            super(source, equipmentResource);
        }
    }

    public static class EditEquipmentEvent extends EquipmentGrid.EquipmentGridEvent {
        EditEquipmentEvent(EquipmentGrid source, EquipmentResource equipmentResource) {
            super(source, equipmentResource);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

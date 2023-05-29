package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.shared.Registration;
import de.dhbw.fireinventory.application.domain.service.appointment.AppointmentResource;
import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.fireinventory.application.domain.service.vehicle.VehicleResource;
import de.dhbw.fireinventory.domain.appointment.Appointment;

import java.util.List;

public class ListViewForm extends FormLayout {

    VirtualList<AppointmentResource> list;

    public ListViewForm(List<AppointmentResource> appointmentResources) {
        list = new VirtualList<>();
        setAppointments(appointmentResources);
        add(list);
    }

    public void setAppointments(List<AppointmentResource> appointmentResources) {
        if (appointmentResources != null) {
            list.setItems(appointmentResources);
            list.setRenderer(ListRenderer);
        }
    }

    private ComponentRenderer<Component, AppointmentResource> ListRenderer = new ComponentRenderer<>(
            appointmentResource -> {
                HorizontalLayout cardLayout = new HorizontalLayout();
                cardLayout.setMargin(true);

                Icon icon = getIcon(appointmentResource.getItemResource());

                VerticalLayout infoLayout = new VerticalLayout();
                infoLayout.setSpacing(false);
                infoLayout.setPadding(false);
                infoLayout.getElement().appendChild(
                        ElementFactory.createStrong(appointmentResource.getDueDate().toString()));
                infoLayout.add(new Div(new Text(appointmentResource.getDesignation())));
                cardLayout.add(icon, infoLayout);
                cardLayout.setAlignItems(FlexComponent.Alignment.CENTER);

                cardLayout.addClickListener(e -> fireEvent(new SelectEvent(this, appointmentResource)));
                return cardLayout;
            });

    private Icon getIcon(ItemResource itemResource) {
        if (itemResource instanceof VehicleResource) {
            return new Icon(VaadinIcon.TRUCK);
        } else {
            return new Icon(VaadinIcon.HAMMER);
        }
    }

    public static abstract class ListViewFormEvent extends ComponentEvent<ListViewForm> {
        private AppointmentResource appointmentResource;

        protected ListViewFormEvent(ListViewForm source, AppointmentResource appointmentResource) {
            super(source, false);
            this.appointmentResource = appointmentResource;
        }

        public AppointmentResource getAppointmentResource() {
            return appointmentResource;
        }
    }

    public static class SelectEvent extends ListViewForm.ListViewFormEvent {
        SelectEvent(ListViewForm source, AppointmentResource appointmentResource) {
            super(source, appointmentResource);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

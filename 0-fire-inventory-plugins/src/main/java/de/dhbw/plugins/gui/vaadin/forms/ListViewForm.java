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
import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import java.util.List;

public class ListViewForm extends FormLayout {

    VirtualList<Appointment> list;

    public ListViewForm(List<Appointment> appointments) {
        list = new VirtualList<>();
        setAppointments(appointments);
        add(list);
    }

    public void setAppointments(List<Appointment> appointments) {
        if (appointments != null) {
            list.setItems(appointments);
            list.setRenderer(ListRenderer);
        }
    }

    private ComponentRenderer<Component, Appointment> ListRenderer = new ComponentRenderer<>(
            appointment -> {
                HorizontalLayout cardLayout = new HorizontalLayout();
                cardLayout.setMargin(true);

                Icon icon = getIcon(appointment.getItem());

                VerticalLayout infoLayout = new VerticalLayout();
                infoLayout.setSpacing(false);
                infoLayout.setPadding(false);
                infoLayout.getElement().appendChild(
                        ElementFactory.createStrong(appointment.getDueDate().toString()));
                infoLayout.add(new Div(new Text(appointment.getDesignation())));
                cardLayout.add(icon, infoLayout);
                cardLayout.setAlignItems(FlexComponent.Alignment.CENTER);

                cardLayout.addClickListener(e -> fireEvent(new SelectEvent(this, appointment)));
                return cardLayout;
            });

    private Icon getIcon(Item item) {
        if (item instanceof Vehicle) {
            return new Icon(VaadinIcon.TRUCK);
        } else {
            return new Icon(VaadinIcon.HAMMER);
        }
    }

    public static abstract class ListViewFormEvent extends ComponentEvent<ListViewForm> {
        private Appointment appointment;

        protected ListViewFormEvent(ListViewForm source, Appointment appointment) {
            super(source, false);
            this.appointment = appointment;
        }

        public Appointment getAppointment() {
            return appointment;
        }
    }

    public static class SelectEvent extends ListViewForm.ListViewFormEvent {
        SelectEvent(ListViewForm source, Appointment appointment) {
            super(source, appointment);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

package de.dhbw.plugins.gui.vaadin.routes;
import com.flowingcode.addons.ycalendar.YearCalendar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.dhbw.fireinventory.adapter.application.appointment.AppointmentAppAdapter;
import de.dhbw.fireinventory.adapter.application.item.ItemAppAdapter;
import de.dhbw.fireinventory.application.domain.service.appointment.AppointmentResource;
import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.plugins.gui.vaadin.MainLayout;
import de.dhbw.plugins.gui.vaadin.forms.AppointmentForm;
import de.dhbw.plugins.gui.vaadin.forms.ListViewForm;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | FireInventory")
public class Calendar extends VerticalLayout {

    private final ItemAppAdapter itemAppAdapter;
    private final AppointmentAppAdapter appointmentAppAdapter;

    TextField filterText = new TextField("Bezeichnung");
    ComboBox<ItemResource> itemComboBox;
    HorizontalLayout filterLayout;

    AppointmentForm appointmentForm;
    ListViewForm listView;
    
    YearCalendar calendar;

    public Calendar(ItemAppAdapter itemAppAdapter, AppointmentAppAdapter appointmentAppAdapter) {
        this.itemAppAdapter = itemAppAdapter;
        this.appointmentAppAdapter = appointmentAppAdapter;
        addClassName("calendar-view");
        addClassName("full-height-layout");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getToolbar(), getContent());
    }

    private HorizontalLayout getContent() {
        configureAppointmentForm();
        configureAppointmentList();

        calendar = new YearCalendar();
        calendar.addDateSelectedListener(e -> createAppointmentForDate(e.getDate()));

        HorizontalLayout content = new HorizontalLayout(calendar, appointmentForm, listView);

        content.setFlexGrow(2, calendar);
        content.setFlexGrow(1, appointmentForm, listView);

        content.addClassNames("content");
        content.setSizeFull();
        content.setHeightFull();

        updateCalendarView();

        return content;
    }

    private VerticalLayout getToolbar() {
        filterText.setPlaceholder("...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateCalendarView());

        Icon filterIcon = new Icon(VaadinIcon.FILTER);
        filterIcon.addClickListener(e -> changeFilterVisibility());

        Button addAppointmentButton = new Button("Termin hinzufügen");

        itemComboBox = new ComboBox<>("Gegenstand");
        itemComboBox.setItems(itemAppAdapter.findAllItems());
        itemComboBox.setItemLabelGenerator(ItemResource::getDesignation);
        itemComboBox.setClearButtonVisible(true);
        itemComboBox.addValueChangeListener(e -> updateCalendarView());

        addAppointmentButton.addClickListener(event -> editAppointment(new AppointmentResource()));

        HorizontalLayout buttonsLayout = new HorizontalLayout(filterIcon, addAppointmentButton);
        buttonsLayout.setAlignItems(Alignment.CENTER);
        filterLayout = new HorizontalLayout(itemComboBox, filterText);
        filterLayout.setVisible(false);

        VerticalLayout toolbar = new VerticalLayout(buttonsLayout, filterLayout);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateCalendarView() {
        List<LocalDate> dates = appointmentAppAdapter.findAppointmentDatesFor(itemComboBox.getValue(), filterText.getValue());
        calendar.setClassNameGenerator(date -> {
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                return "weekend";
            }
            for (LocalDate appointment: dates) {
                if(appointment.compareTo(date) == 0) { return "appointment-set"; }
            }
            return null;
        });
        calendar.refreshAll();
        updateViewList();
        closeAppointmentEditor();
    }

    private void updateViewList() {
        listView.setAppointments(appointmentAppAdapter.findAllAppointmentsFor(itemComboBox.getValue(), filterText.getValue()));
        listView.setVisible(true);
        addClassName("editing");
    }

    private void changeFilterVisibility() {
        if(filterLayout.isVisible()) { filterLayout.setVisible(false); }
        else { filterLayout.setVisible(true); }
    }


    public void editAppointment(AppointmentResource appointmentResource) {
        closeListViewEditor();
        if (appointmentResource == null) {
            closeAppointmentEditor();
        } else {
            appointmentForm.setAppointment(appointmentResource);
            appointmentForm.setVisible(true);
            addClassName("editing");
            if (itemComboBox.getValue() != null) {
                appointmentForm.setItem(itemComboBox.getValue());
            }
        }
    }

    private void createAppointmentForDate(LocalDate date) {
        editAppointment(new AppointmentResource());
        appointmentForm.setDate(date);
    }

    private void closeListViewEditor() {
        listView.setAppointments(null);
        listView.setVisible(false);
        removeClassName("editing");
    }

    private void closeAppointmentEditor() {
        appointmentForm.setAppointment(null);
        appointmentForm.setVisible(false);
        removeClassName("editing");
        updateViewList();
    }

    private void configureAppointmentForm() {
        appointmentForm = new AppointmentForm(itemAppAdapter.findAllItems());
        appointmentForm.setWidth("25em");
        appointmentForm.addListener(AppointmentForm.SaveEvent.class, this::saveAppointment);
        appointmentForm.addListener(AppointmentForm.DeleteEvent.class, this::deleteAppointment);
        appointmentForm.addListener(AppointmentForm.CloseEvent.class, e -> closeAppointmentEditor());
    }

    private void deleteAppointment(AppointmentForm.DeleteEvent event) {
        appointmentAppAdapter.deleteAppointment(event.getAppointmentResource());
    }

    private void saveAppointment(AppointmentForm.SaveEvent event) {
        AppointmentResource appointmentResource = event.getAppointmentResource();
        LocalDate date = appointmentForm.getDueDate();
        appointmentResource.setDueDate(date);
        appointmentAppAdapter.saveAppointment(appointmentResource);
        updateCalendarView();
        closeAppointmentEditor();
    }

    private void configureAppointmentList() {
        listView = new ListViewForm(appointmentAppAdapter.findAllAppointmentsFor(itemComboBox.getValue(), filterText.getValue()));
        listView.addListener(ListViewForm.SelectEvent.class, e -> editAppointment(e.getAppointmentResource()));
        listView.setWidth("25em");
    }
}

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
import de.dhbw.fireinventory.application.appointment.AppointmentApplicationService;
import de.dhbw.fireinventory.application.item.ItemApplicationService;
import de.dhbw.fireinventory.domain.appointment.Appointment;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.plugins.gui.vaadin.MainLayout;
import de.dhbw.plugins.gui.vaadin.forms.AppointmentForm;
import de.dhbw.plugins.gui.vaadin.forms.ListViewForm;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | FireInventory")
public class Calendar extends VerticalLayout {

    private final ItemApplicationService itemService;
    private final AppointmentApplicationService appointmentService;

    TextField filterText = new TextField("Bezeichnung");
    ComboBox<Item> itemComboBox;
    HorizontalLayout filterLayout;

    AppointmentForm appointmentForm;
    ListViewForm listView;
    
    YearCalendar calendar;

    public Calendar(ItemApplicationService itemService, AppointmentApplicationService appointmentService) {
        this.itemService = itemService;
        this.appointmentService = appointmentService;
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

        Button addAppointmentButton = new Button("Add Appointment");

        itemComboBox = new ComboBox<>("Item");
        itemComboBox.setItems(itemService.findAllItems());
        itemComboBox.setItemLabelGenerator(Item::getDesignation);
        itemComboBox.setClearButtonVisible(true);
        itemComboBox.addValueChangeListener(e -> updateCalendarView());

        addAppointmentButton.addClickListener(event -> editAppointment(new Appointment()));

        HorizontalLayout buttonsLayout = new HorizontalLayout(filterIcon, addAppointmentButton);
        buttonsLayout.setAlignItems(Alignment.CENTER);
        filterLayout = new HorizontalLayout(itemComboBox, filterText);
        filterLayout.setVisible(false);

        VerticalLayout toolbar = new VerticalLayout(buttonsLayout, filterLayout);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateCalendarView() {
        List<LocalDate> dates = appointmentService.findAppointmentDatesFor(itemComboBox.getValue(), filterText.getValue());
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
        listView.setAppointments(appointmentService.findAllAppointmentsFor(itemComboBox.getValue(), filterText.getValue()));
        listView.setVisible(true);
        addClassName("editing");
    }

    private void changeFilterVisibility() {
        if(filterLayout.isVisible()) { filterLayout.setVisible(false); }
        else { filterLayout.setVisible(true); }
    }


    public void editAppointment(Appointment appointment) {
        closeListViewEditor();
        if (appointment == null) {
            closeAppointmentEditor();
        } else {
            appointmentForm.setAppointment(appointment);
            appointmentForm.setVisible(true);
            addClassName("editing");
            if (itemComboBox.getValue() != null) {
                appointmentForm.setItem(itemComboBox.getValue());
            }
        }
    }

    private void createAppointmentForDate(LocalDate date) {
        editAppointment(new Appointment());
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
        appointmentForm = new AppointmentForm(itemService.findAllItems());
        appointmentForm.setWidth("25em");
        appointmentForm.addListener(AppointmentForm.SaveEvent.class, this::saveAppointment);
        appointmentForm.addListener(AppointmentForm.DeleteEvent.class, this::deleteAppointment);
        appointmentForm.addListener(AppointmentForm.CloseEvent.class, e -> closeAppointmentEditor());
    }

    private void deleteAppointment(AppointmentForm.DeleteEvent event) {
        appointmentService.deleteAppointment(event.getAppointment());
    }

    private void saveAppointment(AppointmentForm.SaveEvent event) {
        Appointment appointment = event.getAppointment();
        LocalDate date = appointmentForm.getDueDate();
        appointment.setDueDate(date);
        appointmentService.saveAppointment(appointment);
        updateCalendarView();
        closeAppointmentEditor();
    }

    private void configureAppointmentList() {
        listView = new ListViewForm(appointmentService.findAllAppointmentsFor(itemComboBox.getValue(), filterText.getValue()));
        listView.addListener(ListViewForm.SelectEvent.class, e -> editAppointment(e.getAppointment()));
        listView.setWidth("25em");
    }
}

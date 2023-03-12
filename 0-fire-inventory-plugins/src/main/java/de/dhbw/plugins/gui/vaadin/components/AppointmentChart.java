package de.dhbw.plugins.gui.vaadin.components;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AppointmentChart extends VerticalLayout {

    public AppointmentChart(String title) {
        Paragraph titleParagraph = new Paragraph(title);

        HorizontalLayout appointments = new HorizontalLayout();

        add(titleParagraph);
        addClassName("full-height-layout");
    }

    private VerticalLayout createAppointmentElement()
    {
        VerticalLayout appointmentElement = new VerticalLayout();

        return appointmentElement;
    }
}

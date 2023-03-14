package de.dhbw.plugins.gui.vaadin.components;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.dhbw.fireinventory.application.status.StatusApplicationService;
import de.dhbw.plugins.gui.vaadin.tabs.EquipmentTabLayout;

public class StatsBox extends VerticalLayout {

    public StatsBox(String elementDesignation, int allCount, int destroyedCount, StatusApplicationService statusService)
    {
        addClassName("no-padding");

        Paragraph statsTitle = new Paragraph(elementDesignation + " Statistics");

        VerticalLayout allContent = new VerticalLayout();
        allContent.addClickListener(e ->
                allContent.getUI().ifPresent(ui -> ui.navigate(EquipmentTabLayout.class)));

        allContent.addClassName("rounded-border");
        Span allElementLabel = new Span("All " + elementDesignation + " Count");
        Span allElementCount = new Span(String.valueOf(allCount));
        allContent.add(allElementLabel, allElementCount);

        VerticalLayout destroyedContent = new VerticalLayout();
        destroyedContent.addClickListener(e ->
                destroyedContent.getUI().ifPresent(ui ->
                        ui.navigate(EquipmentTabLayout.class).ifPresent(overview ->
                                overview.setStatusComboBox(statusService.getStatusByDesignation("Kaputt")))));
        destroyedContent.addClassName("rounded-border");
        Span destroyedElementLabel = new Span("Destroyed " + elementDesignation + " Count");
        Span destroyedElementCount = new Span(String.valueOf(destroyedCount));
        destroyedContent.add(destroyedElementLabel, destroyedElementCount);

        if(destroyedCount > 0)
        {
            destroyedContent.addClassName("warning");
        }

        add(statsTitle, allContent, destroyedContent);
        addClassName("full-height-layout");
    }
}

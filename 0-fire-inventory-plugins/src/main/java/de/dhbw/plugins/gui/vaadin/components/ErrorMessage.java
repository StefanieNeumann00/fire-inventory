package de.dhbw.plugins.gui.vaadin.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ErrorMessage extends Notification{

    public ErrorMessage(String text)
    {
        addThemeVariants(NotificationVariant.LUMO_ERROR);

        Div textElement = new Div(new Text(text));

        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.addClickListener(event -> {
            close();
        });

        HorizontalLayout layout = new HorizontalLayout(textElement, closeButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        add(layout);
        open();

        setPosition(Notification.Position.MIDDLE);
    }
}

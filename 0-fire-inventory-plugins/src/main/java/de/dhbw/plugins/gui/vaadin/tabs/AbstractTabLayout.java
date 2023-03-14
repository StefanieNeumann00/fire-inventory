package de.dhbw.plugins.gui.vaadin.tabs;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

public abstract class AbstractTabLayout extends VerticalLayout {

    public AbstractTabLayout() {
        setHeightFull();
    }

    protected abstract void configureGrid();

    public static abstract class AbstractTabLayoutEvent extends ComponentEvent<AbstractTabLayout> {

        protected AbstractTabLayoutEvent(AbstractTabLayout source) {
            super(source, false);
        }
    }

    public static class GridUpdatedEvent extends AbstractTabLayout.AbstractTabLayoutEvent {
        GridUpdatedEvent(AbstractTabLayout source) {
            super(source);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

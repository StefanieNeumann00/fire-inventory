package de.dhbw.plugins.gui.vaadin.components;

import com.vaadin.flow.component.grid.Grid;
import de.dhbw.fireinventory.domain.equipment.Equipment;

public abstract class AbstractGrid<T> extends Grid<T> {

    public AbstractGrid(Class className) {
        super(className, false);
        this.setSizeFull();
        this.setHeightFull();
        configureGrid();
    }

    private void configureGrid() {
        this.addClassName("equipment-grid");
        this.setSizeFull();
        this.setHeightFull();
        configureGridColumns();
    }

    protected abstract void configureGridColumns();

    public void clearGridSelection() {
        this.asSingleSelect().clear();
    }
}

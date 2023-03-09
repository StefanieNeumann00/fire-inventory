package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;

public interface FormDialog {
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    RadioButtonGroup<String> conditionRadioGroup = new RadioButtonGroup<>();

    default HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> closeDialog());

        return new HorizontalLayout(save, close);
    }

    public void validateAndSave();

    public void closeDialog();

    default void createConditionRadioButton()
    {
        conditionRadioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        conditionRadioGroup.setLabel("Zustand");
        conditionRadioGroup.setItems("funktionsfähig", "nicht funktionsfähig");
        conditionRadioGroup.setValue("funktionsfähig");
    }
}

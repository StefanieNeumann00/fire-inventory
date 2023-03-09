package de.dhbw.plugins.gui.vaadin.forms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;

public interface FormDialog {
    RadioButtonGroup<String> conditionRadioGroup = new RadioButtonGroup<>();

    public void validateAndSave();

    default void createConditionRadioButton()
    {
        conditionRadioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        conditionRadioGroup.setLabel("Zustand");
        conditionRadioGroup.setItems("funktionsfähig", "nicht funktionsfähig");
        conditionRadioGroup.setValue("funktionsfähig");
    }
}

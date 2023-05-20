package de.dhbw.fireinventory.domain.condition;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ConditionConverter implements AttributeConverter<Condition, String> {

    @Override
    public String convertToDatabaseColumn(Condition condition) {
        if(condition == null) {
            return null;
        }
        return condition.getCondition();
    }

    @Override
    public Condition convertToEntityAttribute(String string) {
        if (string == null) {
            return null;
        }

        return Stream.of(Condition.values())
                .filter(c -> c.getCondition().equals(string))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

package de.dhbw.fireinventory.domain.status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, String> {

    @Override
    public String convertToDatabaseColumn(Status status) {
        if(status == null) {
            return null;
        }
        return status.getDesignation();
    }

    @Override
    public Status convertToEntityAttribute(String string) {
        if (string == null) {
            return null;
        }

        return Stream.of(Status.values())
                .filter(s -> s.getDesignation().equals(string))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

package de.dhbw.fireinventory.adapter.status;

import de.dhbw.fireinventory.adapter.location.LocationResource;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StatusToStatusResourceMapper implements Function<Status, StatusResource> {

    @Override
    public StatusResource apply(final Status status) {
        return map(status);
    }

    private StatusResource map(final Status status) {
        return new StatusResource(status.getDesignation());
    }

}

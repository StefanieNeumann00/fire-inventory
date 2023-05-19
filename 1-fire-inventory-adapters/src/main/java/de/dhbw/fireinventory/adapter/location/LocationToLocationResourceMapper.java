package de.dhbw.fireinventory.adapter.location;

import de.dhbw.fireinventory.adapter.equipment.EquipmentResource;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class LocationToLocationResourceMapper implements Function<Location, LocationResource> {

    @Override
    public LocationResource apply(final Location location) {
        return map(location);
    }

    private LocationResource map(final Location location) {
        return new LocationResource(location.getDesignation());
    }

}

package de.dhbw.fireinventory.adapter.mapper.location;

import de.dhbw.fireinventory.application.domain.service.externalPlace.ExternalPlaceResource;
import de.dhbw.fireinventory.application.domain.service.internalPlace.InternalPlaceResource;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.application.domain.service.vehiclePlace.VehiclePlaceResource;
import de.dhbw.fireinventory.domain.location.ExternalPlace;
import de.dhbw.fireinventory.domain.location.InternalPlace;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.VehiclePlace;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class LocationToLocationResourceMapper implements Function<Location, LocationResource> {

    @Override
    public LocationResource apply(final Location location) {
        return map(location);
    }

    private LocationResource map(final Location location) {
        if(location instanceof InternalPlace) {
            return new InternalPlaceResource(location.getId(), location.getDesignation());
        }
        else if (location instanceof ExternalPlace) {
            return new ExternalPlaceResource(location.getId(), location.getDesignation());
        }
        else {
            VehiclePlace vehiclePlace = (VehiclePlace)location;
            return new VehiclePlaceResource(vehiclePlace.getId(), vehiclePlace.getDesignation(), vehiclePlace.getVehicle());
        }
    }

}

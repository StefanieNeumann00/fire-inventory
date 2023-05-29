package de.dhbw.fireinventory.adapter.mapper.location;

import de.dhbw.fireinventory.application.domain.service.externalPlace.ExternalPlaceResource;
import de.dhbw.fireinventory.application.domain.service.internalPlace.InternalPlaceResource;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.application.domain.service.vehicle.VehicleResource;
import de.dhbw.fireinventory.application.domain.service.vehiclePlace.VehiclePlaceResource;
import de.dhbw.fireinventory.domain.location.ExternalPlace;
import de.dhbw.fireinventory.domain.location.InternalPlace;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.VehiclePlace;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class LocationResourceToLocationMapper implements Function<LocationResource, Location> {

    @Override
    public Location apply(final LocationResource locationResource) {
        return map(locationResource);
    }

    private Location map(final LocationResource locationResource) {

        if(locationResource instanceof InternalPlaceResource) {
            InternalPlaceResource internalPlaceResource = (InternalPlaceResource) locationResource;
            InternalPlace internalPlace = new InternalPlace();
            internalPlace.setId(internalPlaceResource.getId());
            internalPlace.setDesignation(internalPlaceResource.getDesignation());

            return internalPlace;
        }
        else if (locationResource instanceof ExternalPlaceResource) {
            ExternalPlaceResource externalPlaceResource = (ExternalPlaceResource) locationResource;
            ExternalPlace externalPlace = new ExternalPlace();
            externalPlace.setId(externalPlaceResource.getId());
            externalPlace.setDesignation(externalPlaceResource.getDesignation());

            return externalPlace;
        }
        else if (locationResource instanceof VehiclePlaceResource){
            VehiclePlaceResource vehiclePlaceResource = (VehiclePlaceResource) locationResource;
            VehiclePlace vehiclePlace = new VehiclePlace();
            vehiclePlace.setId(vehiclePlaceResource.getId());
            vehiclePlace.setDesignation(vehiclePlaceResource.getDesignation());
            vehiclePlace.setVehicle(vehiclePlaceResource.getVehicle());

            return vehiclePlace;
        }
        else {
            return null;
        }
    }
}

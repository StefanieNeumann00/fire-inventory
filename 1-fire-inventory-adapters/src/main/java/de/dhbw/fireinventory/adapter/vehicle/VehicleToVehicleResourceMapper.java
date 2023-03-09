package de.dhbw.fireinventory.adapter.vehicle;

import de.dhbw.fireinventory.adapter.location.LocationResource;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class VehicleToVehicleResourceMapper implements Function<Vehicle, VehicleResource> {

    @Override
    public VehicleResource apply(final Vehicle vehicle) {
        return map(vehicle);
    }

    private VehicleResource map(final Vehicle vehicle) {
        return new VehicleResource(vehicle.getDesignation(), vehicle.getStatus(), vehicle.getPlace());
    }

}

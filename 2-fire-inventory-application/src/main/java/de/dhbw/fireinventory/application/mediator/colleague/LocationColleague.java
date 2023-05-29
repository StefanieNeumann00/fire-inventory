package de.dhbw.fireinventory.application.mediator.colleague;

import de.dhbw.fireinventory.application.ConcreteApplicationMediator;
import de.dhbw.fireinventory.application.domain.service.location.LocationDomainService;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.VehiclePlace;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.core.annotation.Order;

public class LocationColleague extends Colleague{

    private final LocationDomainService locationDomainService;

    protected LocationColleague(final ConcreteApplicationMediator mediator, final LocationDomainService locationDomainService) {
        super(mediator, 0);
        this.locationDomainService = locationDomainService;
    }

    @Override
    public void onDeleteLocation(Location location) throws HasLinkedException {
        if (!locationDomainService.hasLinkedItems(location)) {
            locationDomainService.deleteLocation(location);
        }
        else {
            throw new HasLinkedException();
        }
    }

    @Override
    public void onSaveVehicle(Item item, Condition condition) {
        if (item instanceof Vehicle) {
            VehiclePlace vehiclePlace = new VehiclePlace();
            vehiclePlace.setDesignation(item.getDesignation());
            vehiclePlace.setVehicle((Vehicle)item);
            locationDomainService.saveLocation(vehiclePlace);
        }
    }

    @Override
    public void onDeleteVehicle(Item item) {
        if (item instanceof Vehicle) {
            Location location = locationDomainService.findLocationForVehicle((Vehicle)item);
            locationDomainService.deleteLocation(location);
        }

    }
}

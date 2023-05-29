package de.dhbw.fireinventory.application.mediator.service.location;

import de.dhbw.fireinventory.application.ConcreteApplicationMediator;
import de.dhbw.fireinventory.application.domain.service.location.LocationDomainService;
import de.dhbw.fireinventory.application.mediator.colleague.HasLinkedException;
import de.dhbw.fireinventory.application.mediator.colleague.LocationColleague;
import de.dhbw.fireinventory.domain.location.Location;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LocationOperationService extends LocationColleague implements LocationService {

    private final LocationDomainService locationDomainService;

    public LocationOperationService(ConcreteApplicationMediator mediator, LocationDomainService locationDomainService) {
        super(mediator, locationDomainService);
        this.locationDomainService = locationDomainService;
    }

    public boolean deleteLocation(Location location) {
        try {
            onDeleteLocation(location);
            getMediator().onDeleteLocation(location, this);
        }
        catch (HasLinkedException e) {
            return false;
        }
        return true;
    }
}

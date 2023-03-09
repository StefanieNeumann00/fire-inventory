package de.dhbw.fireinventory.application.location;

import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.LocationRepository;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.place.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationApplicationService {

    private final LocationRepository locationRepository;
    @Autowired
    public LocationApplicationService(final LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> findAllLocations() {
        return this.locationRepository.findAllLocations();
    }

    public void saveLocation(Location location){ this.locationRepository.save(location); }
}

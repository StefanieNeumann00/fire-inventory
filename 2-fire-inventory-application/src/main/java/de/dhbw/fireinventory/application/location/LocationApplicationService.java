package de.dhbw.fireinventory.application.location;

import de.dhbw.fireinventory.domain.location.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationApplicationService {

    private final LocationRepository locationRepository;
    @Autowired
    public LocationApplicationService(final LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void saveLocation(Location location){ this.locationRepository.save(location); }

    public void deleteLocation(Location location) {
        this.locationRepository.delete(location);
    }

    public List<Location> findAllLocations(String filterText) {
        if (filterText == null) {
            return this.locationRepository.findAll();
        } else {
            return this.locationRepository.findAllBy(filterText);
        }
    }

    public List<Location> findAllPlaces(String filterText) {
        List<Location> allLocations = this.findAllLocations(filterText);
        List<Location> allPlaces = new ArrayList<>();

        for (Location location: allLocations) {
            if(location instanceof InternalPlace || location instanceof ExternalPlace){
                allPlaces.add(location);
            }
        }

        return allPlaces;
    }
}

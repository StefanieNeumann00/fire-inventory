package de.dhbw.fireinventory.application.domain.service.location;

import de.dhbw.fireinventory.domain.location.*;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationApplicationService implements LocationDomainService{

    private final LocationRepository locationRepository;

    @Autowired
    public LocationApplicationService(final LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void saveLocation(Location location){ this.locationRepository.save(location); }

    @Override
    public void deleteLocation(Location location) {
        if (this.isPresent(location)) {
            this.locationRepository.delete(location);
        }
    }

    @Override
    public boolean hasLinkedItems(Location location) {
        return this.locationRepository.hasLinkedItems(location);
    }

    @Override
    public List<Location> findAllLocations(String filterText) {
        if (filterText == null) {
            return this.locationRepository.findAll();
        } else {
            return this.locationRepository.findAllBy(filterText);
        }
    }

    @Override
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

    @Override
    public Location findLocationForVehicle(Vehicle vehicle) {
        return this.locationRepository.findLocationForVehicle(vehicle);
    }

    private boolean isPresent(Location location) {
        return  this.locationRepository.isPresent(location);
    }
}

package de.dhbw.fireinventory.application.location;

import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.LocationRepository;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.place.PlaceRepository;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
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

    public List<Location> findAllLocations() {
        return this.locationRepository.findAllLocations();
    }

    public void saveLocation(Location location){ this.locationRepository.save(location); }

    public Location getLocationForVehicle(Vehicle vehicle) { return this.locationRepository.getLocationForVehicle(vehicle); }

    public Location getLocationForPlace(Place place) { return this.locationRepository.getLocationForPlace(place); }

    public void deleteLocation(Place place) {
        Location location = this.getLocationForPlace(place);
        this.locationRepository.deleteLocation(location);
    }

    public void deleteLocation(Vehicle vehicle) {
        Location location = this.getLocationForVehicle(vehicle);
        this.locationRepository.deleteLocation(location);
    }
}

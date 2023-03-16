package de.dhbw.plugins.persistence.hibernate.location;

import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.LocationRepository;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationRepositoryBridge implements LocationRepository {

    private final SpringDataLocationRepository springDataLocationRepository;

    @Autowired
    public LocationRepositoryBridge(final SpringDataLocationRepository springDataLocationRepository) {
        this.springDataLocationRepository = springDataLocationRepository;
    }

    @Override
    public List<Location> findAllLocations() {
        return this.springDataLocationRepository.findAll();
    }

    @Override
    public Location save(final Location location) {
        return this.springDataLocationRepository.save(location);
    }

    @Override
    public Location getLocationForVehicle(Vehicle vehicle) {
        return this.springDataLocationRepository.getLocationForVehicle(vehicle);
    }

    @Override
    public Location getLocationForPlace(Place place) {
        return this.springDataLocationRepository.getLocationForPlace(place);
    }

    @Override
    public void deleteLocation(Location location) {
        this.springDataLocationRepository.delete(location);
    }
}

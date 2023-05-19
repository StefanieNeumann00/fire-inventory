package de.dhbw.plugins.persistence.hibernate.location;

import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.LocationRepository;
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
    public Location save(final Location location) {
        return this.springDataLocationRepository.save(location);
    }


    @Override
    public void delete(Location location) {
        this.springDataLocationRepository.delete(location);
    }

    @Override
    public List<Location> findAll(String filterText) {
        return this.springDataLocationRepository.findAllBy(filterText);
    }


}

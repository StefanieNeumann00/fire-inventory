package de.dhbw.plugins.persistence.hibernate.location;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.LocationRepository;
import de.dhbw.plugins.persistence.hibernate.equipment.SpringDataEquipmentRepository;
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
}

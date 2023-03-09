package de.dhbw.plugins.persistence.hibernate.place;

import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.place.PlaceRepository;
import de.dhbw.plugins.persistence.hibernate.location.SpringDataLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlaceRepositoryBridge implements PlaceRepository {

    private final SpringDataPlaceRepository springDataPlaceRepository;

    @Autowired
    public PlaceRepositoryBridge(final SpringDataPlaceRepository springDataPlaceRepository) {
        this.springDataPlaceRepository = springDataPlaceRepository;
    }

    @Override
    public List<Place> findAllPlaces() {
        return this.springDataPlaceRepository.findAll();
    }

    @Override
    public Place save(final Place place) {
        return this.springDataPlaceRepository.save(place);
    }
}


package de.dhbw.fireinventory.application.place;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.equipment.EquipmentRepository;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.place.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceApplicationService {

    private final PlaceRepository placeRepository;
    @Autowired
    public PlaceApplicationService(final PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> findAllPlaces() {
        return this.placeRepository.findAllPlaces();
    }

    public void savePlace(Place place){ this.placeRepository.save(place); }
}


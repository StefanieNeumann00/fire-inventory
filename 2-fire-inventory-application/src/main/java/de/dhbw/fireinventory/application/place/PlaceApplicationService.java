package de.dhbw.fireinventory.application.place;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.equipment.EquipmentRepository;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.LocationRepository;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.place.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceApplicationService {

    private final PlaceRepository placeRepository;
    private final LocationRepository locationRepository;
    @Autowired
    public PlaceApplicationService(final PlaceRepository placeRepository, final LocationRepository locationRepository) {
        this.placeRepository = placeRepository;
        this.locationRepository = locationRepository;
    }

    public List<Place> findAllBy(String designation) {
        return this.placeRepository.findAllBy(designation);
    }

    public void savePlace(Place place){
        this.placeRepository.save(place);
        Location location = new Location();
        location.setPlace(place);
        this.locationRepository.save(location);
    }

    public void deletePlace(Place place) {
        this.placeRepository.delete(place);
    }
}


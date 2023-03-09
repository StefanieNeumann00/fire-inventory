package de.dhbw.fireinventory.domain.place;

import de.dhbw.fireinventory.domain.location.Location;

import java.util.List;

public interface PlaceRepository {

    List<Place> findAllPlaces();

    Place save(Place place);
}

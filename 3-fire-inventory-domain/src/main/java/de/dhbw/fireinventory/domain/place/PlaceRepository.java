package de.dhbw.fireinventory.domain.place;

import de.dhbw.fireinventory.domain.location.Location;

import java.util.List;

public interface PlaceRepository {

    List<Place> findAllBy(String designation);

    Place save(Place place);

    void delete(Place place);
}

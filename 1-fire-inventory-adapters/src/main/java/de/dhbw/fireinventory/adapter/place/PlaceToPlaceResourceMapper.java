package de.dhbw.fireinventory.adapter.place;

import de.dhbw.fireinventory.adapter.location.LocationResource;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.place.Place;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PlaceToPlaceResourceMapper implements Function<Place, PlaceResource> {

    @Override
    public PlaceResource apply(final Place place) {
        return map(place);
    }

    private PlaceResource map(final Place place) {
        return new PlaceResource(place.getDesignation());
    }
}

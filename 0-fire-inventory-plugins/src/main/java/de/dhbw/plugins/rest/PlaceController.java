package de.dhbw.plugins.rest;

import de.dhbw.fireinventory.adapter.place.PlaceResource;
import de.dhbw.fireinventory.adapter.place.PlaceToPlaceResourceMapper;
import de.dhbw.fireinventory.application.place.PlaceApplicationService;
import de.dhbw.fireinventory.domain.place.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/place")
public class PlaceController {

    private final PlaceApplicationService placeApplicationService;

    private final PlaceToPlaceResourceMapper placeToPlaceResourceMapper;

    @Autowired
    public PlaceController(final PlaceApplicationService placeApplicationService, final PlaceToPlaceResourceMapper placeToPlaceResourceMapper) {
        this.placeApplicationService = placeApplicationService;
        this.placeToPlaceResourceMapper = placeToPlaceResourceMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PlaceResource> getPlaces() {
        return this.placeApplicationService.findAllPlaces().stream()
                .map(placeToPlaceResourceMapper)
                .collect(Collectors.toList());
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public void addPlace(@RequestBody Place place) {
        this.placeApplicationService.savePlace(place);
    }

}

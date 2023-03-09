package de.dhbw.plugins.rest;

import de.dhbw.fireinventory.adapter.equipment.EquipmentResource;
import de.dhbw.fireinventory.adapter.equipment.EquipmentToEquipmentResourceMapper;
import de.dhbw.fireinventory.adapter.location.LocationResource;
import de.dhbw.fireinventory.adapter.location.LocationToLocationResourceMapper;
import de.dhbw.fireinventory.application.equipment.EquipmentApplicationService;
import de.dhbw.fireinventory.application.location.LocationApplicationService;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/location")
public class LocationController {

    private final LocationApplicationService locationApplicationService;

    private final LocationToLocationResourceMapper locationToLocationResourceMapper;

    @Autowired
    public LocationController(final LocationApplicationService locationApplicationService, final LocationToLocationResourceMapper locationToLocationResourceMapper) {
        this.locationApplicationService = locationApplicationService;
        this.locationToLocationResourceMapper = locationToLocationResourceMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<LocationResource> getLocations() {
        return this.locationApplicationService.findAllLocations().stream()
                .map(locationToLocationResourceMapper)
                .collect(Collectors.toList());
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public void addLocation(@RequestBody Location location) {
        this.locationApplicationService.saveLocation(location);
    }

}

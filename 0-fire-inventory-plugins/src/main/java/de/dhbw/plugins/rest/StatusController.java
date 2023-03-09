package de.dhbw.plugins.rest;

import de.dhbw.fireinventory.adapter.place.PlaceResource;
import de.dhbw.fireinventory.adapter.place.PlaceToPlaceResourceMapper;
import de.dhbw.fireinventory.adapter.status.StatusResource;
import de.dhbw.fireinventory.adapter.status.StatusToStatusResourceMapper;
import de.dhbw.fireinventory.application.place.PlaceApplicationService;
import de.dhbw.fireinventory.application.status.StatusApplicationService;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/status")
public class StatusController {

    private final StatusApplicationService statusApplicationService;

    private final StatusToStatusResourceMapper statusToStatusResourceMapper;

    @Autowired
    public StatusController(final StatusApplicationService statusApplicationService, final StatusToStatusResourceMapper statusToStatusResourceMapper) {
        this.statusApplicationService = statusApplicationService;
        this.statusToStatusResourceMapper = statusToStatusResourceMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<StatusResource> getStatuses() {
        return this.statusApplicationService.findAllStatuses().stream()
                .map(statusToStatusResourceMapper)
                .collect(Collectors.toList());
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public void addStatus(@RequestBody Status status) {
        this.statusApplicationService.saveStatus(status);
    }

}

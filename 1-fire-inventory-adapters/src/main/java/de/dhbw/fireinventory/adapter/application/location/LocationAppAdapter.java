package de.dhbw.fireinventory.adapter.application.location;

import de.dhbw.fireinventory.adapter.mapper.location.LocationResourceToLocationMapper;
import de.dhbw.fireinventory.adapter.mapper.location.LocationToLocationResourceMapper;
import de.dhbw.fireinventory.application.domain.service.internalPlace.InternalPlaceResource;
import de.dhbw.fireinventory.application.domain.service.location.LocationDomainService;
import de.dhbw.fireinventory.application.domain.service.location.LocationDomainServicePort;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.application.mediator.service.location.LocationServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationAppAdapter implements LocationApplicationAdapter {

    private final LocationDomainServicePort locationDomainServicePort;
    private final LocationServicePort locationServicePort;
    private final LocationResourceToLocationMapper locationResourceToLocationMapper;
    private final LocationToLocationResourceMapper locationToLocationResourceMapper;

    @Override
    public void saveLocation(LocationResource locationResource) {
        this.locationDomainServicePort.saveLocation(locationResourceToLocationMapper.apply(locationResource));
    }

    @Override
    public void deleteLocation(LocationResource locationResource) {
        this.locationServicePort.deleteLocation(locationResourceToLocationMapper.apply(locationResource));
    }

    @Override
    public List<LocationResource> findAllLocations(String filterText) {
        return this.locationDomainServicePort.findAllLocations(filterText).stream()
                .map(locationToLocationResourceMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<LocationResource> findAllPlaces(String filterText) {
        return this.locationDomainServicePort.findAllPlaces(filterText).stream()
                .map(locationToLocationResourceMapper)
                .collect(Collectors.toList());
    }

}

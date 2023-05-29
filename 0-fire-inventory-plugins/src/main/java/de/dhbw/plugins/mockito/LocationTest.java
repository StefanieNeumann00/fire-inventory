package de.dhbw.plugins.mockito;

import static org.mockito.Mockito.when;
import de.dhbw.fireinventory.application.domain.service.location.LocationApplicationService;
import de.dhbw.fireinventory.domain.location.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LocationTest {

    @Mock
    LocationRepository locationRepository;

    LocationApplicationService applicationService;

    @Before
    public void initiate() {
        applicationService = new LocationApplicationService(locationRepository);
    }


    @Test
    public void testGetAllPlaces() {
        Location internalPlace = new InternalPlace();
        Location externalPlace = new ExternalPlace();
        Location vehiclePlace = new VehiclePlace();

        List<Location> allLocations = new ArrayList<>();
        allLocations.add(internalPlace);
        allLocations.add(externalPlace);
        allLocations.add(vehiclePlace);

        when(locationRepository.findAll()).thenReturn(allLocations);

        List<Location> returnList = applicationService.findAllPlaces(null);

        assertEquals(2, returnList.size());
    }
}

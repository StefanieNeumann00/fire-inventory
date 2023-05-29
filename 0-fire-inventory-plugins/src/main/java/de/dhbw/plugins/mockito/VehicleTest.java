package de.dhbw.plugins.mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.dhbw.fireinventory.application.domain.service.item.ItemApplicationService;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.item.ItemRepository;
import de.dhbw.fireinventory.domain.location.*;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VehicleTest {

    @Mock
    ItemRepository itemRepository;

    @Mock
    LocationRepository locationRepository;

    ItemApplicationService applicationService;

    @Before
    public void initiate() {
        applicationService = new ItemApplicationService(itemRepository);
    }


    @Test
    public void testChangeCondition1() {
        Location location = new InternalPlace();

        Vehicle vehicle = new Vehicle();
        vehicle.setLocation(location);
        vehicle.setCondition(Condition.FUNKTIONSFÄHIG);

        applicationService.changeCondition(vehicle);

        assertEquals(Condition.NICHT_FUNKTIONSFÄHIG, vehicle.getCondition());
    }

    @Test
    public void testChangeCondition2() {
        Location location = new InternalPlace();

        Vehicle vehicle = new Vehicle();
        vehicle.setLocation(location);
        vehicle.setCondition(Condition.NICHT_FUNKTIONSFÄHIG);

        applicationService.changeCondition(vehicle);

        assertEquals(Condition.IN_REPARATUR, vehicle.getCondition());
    }

    @Test
    public void testCalculateStatus1() {
        Location location = new InternalPlace();

        Vehicle vehicle = new Vehicle();
        vehicle.setLocation(location);
        vehicle.setCondition(Condition.NICHT_FUNKTIONSFÄHIG);

        assertEquals(Status.KAPUTT, vehicle.getStatus());
    }

    @Test
    public void testCalculateStatus2() {
        Location location = new ExternalPlace();

        Vehicle vehicle = new Vehicle();
        vehicle.setLocation(location);
        vehicle.setCondition(Condition.FUNKTIONSFÄHIG);

        assertEquals(Status.NICHT_VOR_ORT, vehicle.getStatus());
    }
}

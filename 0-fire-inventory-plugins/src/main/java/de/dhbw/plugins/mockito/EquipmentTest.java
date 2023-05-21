package de.dhbw.plugins.mockito;

import de.dhbw.fireinventory.application.equipment.EquipmentApplicationService;
import de.dhbw.fireinventory.application.vehicle.VehicleApplicationService;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.item.ItemRepository;
import de.dhbw.fireinventory.domain.location.InternalPlace;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.LocationRepository;
import de.dhbw.fireinventory.domain.location.VehiclePlace;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentTest {

    @Mock
    ItemRepository itemRepository;

    EquipmentApplicationService applicationService;

    @Before
    public void initiate() {
        applicationService = new EquipmentApplicationService(itemRepository);
    }


    @Test
    public void testChangeCondition1() {
        Location location = new InternalPlace();
        Equipment equipment = new Equipment();
        equipment.setLocation(location);
        equipment.setCondition(Condition.FUNKTIONSFÄHIG);

        applicationService.changeCondition(equipment);

        assertEquals(Condition.NICHT_FUNKTIONSFÄHIG, equipment.getCondition());
    }

    @Test
    public void testChangeCondition2() {
        Location location = new InternalPlace();
        Equipment equipment = new Equipment();
        equipment.setLocation(location);
        equipment.setCondition(Condition.NICHT_FUNKTIONSFÄHIG);

        applicationService.changeCondition(equipment);

        assertEquals(Condition.IN_REPARATUR, equipment.getCondition());
    }

    @Test
    public void testCalculateStatus1() {
        Location location = new InternalPlace();
        Equipment equipment = new Equipment();
        equipment.setLocation(location);
        equipment.setCondition(Condition.NICHT_FUNKTIONSFÄHIG);

        assertEquals(Status.KAPUTT, equipment.getStatus());
    }

    @Test
    public void testCalculateStatus2() {
        Vehicle vehicle = new Vehicle();
        vehicle.setCondition(Condition.FUNKTIONSFÄHIG);
        vehicle.setLocation(new InternalPlace());

        VehiclePlace location = new VehiclePlace();
        location.setVehicle(vehicle);

        Equipment equipment = new Equipment();
        equipment.setCondition(Condition.FUNKTIONSFÄHIG);
        equipment.setLocation(location);

        assertEquals(Status.EINSATZBEREIT, equipment.getStatus());
    }
}

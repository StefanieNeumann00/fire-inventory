package de.dhbw.fireinventory.adapter.application.vehicle;

import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import de.dhbw.fireinventory.application.domain.service.location.LocationResource;
import de.dhbw.fireinventory.application.domain.service.vehicle.VehicleResource;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.status.Status;
import java.util.List;

public interface VehicleApplicationAdapter {

    List<ItemResource> findAllVehicles(String filterText);

    List<ItemResource> findAllVehiclesBy(LocationResource locationResource, Status status, String designation);

    void saveVehicle(VehicleResource vehicleResource, Condition condition);

    void deleteVehicle(VehicleResource vehicleResource);

    void changeCondition(VehicleResource vehicleResource);

    int getVehicleCount();

    int vehicleStatusCount(Status status);
}

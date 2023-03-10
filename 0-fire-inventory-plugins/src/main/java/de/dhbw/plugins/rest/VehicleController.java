package de.dhbw.plugins.rest;

import de.dhbw.fireinventory.adapter.vehicle.VehicleResource;
import de.dhbw.fireinventory.adapter.vehicle.VehicleToVehicleResourceMapper;
import de.dhbw.fireinventory.application.vehicle.VehicleApplicationService;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/vehicle")
public class VehicleController {

    private final VehicleApplicationService vehicleApplicationService;

    private final VehicleToVehicleResourceMapper vehicleToVehicleResourceMapper;

    @Autowired
    public VehicleController(final VehicleApplicationService vehicleApplicationService, final VehicleToVehicleResourceMapper vehicleToVehicleResourceMapper) {
        this.vehicleApplicationService = vehicleApplicationService;
        this.vehicleToVehicleResourceMapper = vehicleToVehicleResourceMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<VehicleResource> getVehicles() {
        return this.vehicleApplicationService.findAllVehicles().stream()
                .map(vehicleToVehicleResourceMapper)
                .collect(Collectors.toList());
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public void addVehicle(@RequestBody Vehicle vehicle) {
        this.vehicleApplicationService.saveVehicle(vehicle);
    }

}

package de.dhbw.fireinventory.application.vehicle;

import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.location.LocationRepository;
import de.dhbw.fireinventory.domain.location.VehiclePlace;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.item.ItemRepository;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleApplicationService {

    private final ItemRepository itemRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public VehicleApplicationService(final ItemRepository itemRepository, final LocationRepository locationRepository) {
        this.itemRepository = itemRepository;
        this.locationRepository = locationRepository;
    }

    public List<Vehicle> findAllVehicles(String filterText) {
        return this.filterVehicle(this.itemRepository.findAllBy(filterText));
    }

    public List<Vehicle> findAllVehiclesBy(Location location, Status status, String designation) {
        if (location == null && status == null) { return this.findAllVehicles(designation); }
        else if (location != null && status == null && designation.isEmpty()) { return this.filterVehicle(this.itemRepository.findAllByLocation(location)); }
        else if (location != null && status == null && !designation.isEmpty()) { return this.filterVehicle(this.itemRepository.findAllByLocationAndDesignation(location, designation)); }
        else if (location != null && status != null && designation.isEmpty()) { return this.filterVehicle(this.forStatus(this.itemRepository.findAllByLocation(location), status)); }
        else if (location != null && status != null && !designation.isEmpty()) { return this.filterVehicle(this.forStatus(this.itemRepository.findAllByLocationAndDesignation(location, designation), status));}
        else if (location == null && status != null && designation.isEmpty()) { return this.filterVehicle(this.forStatus(this.itemRepository.findAll(), status)); }
        else { return this.filterVehicle(this.forStatus(this.itemRepository.findAllBy(designation), status));}
    }

    private List<Item> forStatus(List<Item> items, Status status) {
        List<Item> resultSet = new ArrayList<>();
        for (Item item: items) {
            if(item.getStatus() == status) {
                resultSet.add(item);
            }
        }
        return resultSet;
    }

    public void saveVehicle(Vehicle vehicle){ this.itemRepository.save(vehicle);}

    public void saveVehicle(Vehicle vehicle, Condition condition){
        if (vehicle.getCondition() == null) {
            vehicle.setCondition(Condition.FUNKTIONSFÄHIG);
        }
        this.saveVehicle(vehicle);
        VehiclePlace vehiclePlace = new VehiclePlace();
        vehiclePlace.setDesignation(vehicle.getDesignation());
        vehiclePlace.setVehicle(vehicle);
        this.locationRepository.save(vehiclePlace);
    }

    public void deleteVehicle(Vehicle vehicle) {
        this.itemRepository.delete(vehicle);
        VehiclePlace vehiclePlace = this.locationRepository.findLocationForVehicle(vehicle);
        this.locationRepository.delete(vehiclePlace);
    }

    public void changeCondition(Vehicle vehicle) {
        if (vehicle.getCondition() == Condition.FUNKTIONSFÄHIG) {
            vehicle.setCondition(Condition.NICHT_FUNKTIONSFÄHIG);
        }
        else if (vehicle.getCondition() == Condition.NICHT_FUNKTIONSFÄHIG) {
            vehicle.setCondition(Condition.IN_REPARATUR);
        }
        else {vehicle.setCondition(Condition.FUNKTIONSFÄHIG);}
        this.saveVehicle(vehicle);
    }

    public int getVehicleCount(){ return this.findAllVehicles(null).size();}

    public int vehicleStatusCount(Status status) { return this.filterVehicle(this.forStatus(this.itemRepository.findAll(), status)).size();}

    private List<Vehicle> filterVehicle(List<Item> items)
    {
        List<Vehicle> vehicles = items.stream()
                .filter(i -> i instanceof Vehicle)
                .map(i -> (Vehicle) i)
                .collect(Collectors.toList());

        return vehicles;
    }
}

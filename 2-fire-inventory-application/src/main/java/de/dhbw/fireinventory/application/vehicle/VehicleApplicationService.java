package de.dhbw.fireinventory.application.vehicle;

import de.dhbw.fireinventory.domain.Condition;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.item.ItemRepository;
import de.dhbw.fireinventory.domain.location.InternalPlace;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleApplicationService {

    private final ItemRepository itemRepository;

    @Autowired
    public VehicleApplicationService(final ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Vehicle> findAllVehicles(String filterText) {
        return this.filterVehicle(this.itemRepository.findAllBy(filterText));
    }

    public List<Vehicle> findAllVehiclesBy(Location location, Status status, String designation) {
        if (location == null && status == null) { return this.findAllVehicles(designation); }
        else if (location != null && status == null && designation.isEmpty()) { return this.filterVehicle(this.itemRepository.findAllByLocation(location)); }
        else if (location != null && status == null && !designation.isEmpty()) { return this.filterVehicle(this.itemRepository.findAllByLocationAndDesignation(location, designation)); }
        else if (location != null && status != null && designation.isEmpty()) { return this.filterVehicle(this.itemRepository.findAllByLocationAndStatus(location, status)); }
        else if (location != null && status != null && !designation.isEmpty()) { return this.filterVehicle(this.itemRepository.findAllByLocationStatusAndDesignation(location, status, designation));}
        else if (location == null && status != null && designation.isEmpty()) { return this.filterVehicle(this.itemRepository.findAllByStatus(status)); }
        else { return this.filterVehicle(this.itemRepository.findAllByStatusAndDesignation(status, designation));}

    }

    public void saveVehicle(Vehicle vehicle){ this.itemRepository.save(vehicle);}

    public void saveVehicle(Vehicle vehicle, Condition condition){
        if(condition == Condition.FUNKTIONSFÄHIG){
            if(vehicle.getLocation() instanceof InternalPlace){
                vehicle.setStatus(Status.EINSATZBEREIT);
            }
            else {vehicle.setStatus(Status.NICHT_VOR_ORT);}
        }
        else if(condition == Condition.NICHT_FUNKTIONSFÄHIG) {vehicle.setStatus(Status.KAPUTT);}
        else {vehicle.setStatus(Status.IN_REPARATUR);}
        this.saveVehicle(vehicle);
    }

    public void deleteVehicle(Vehicle vehicle) { this.itemRepository.delete(vehicle); }

    public int getVehicleCount(){ return this.findAllVehicles(null).size();}

    public int vehicleStatusCount(Status status) { return this.filterVehicle(this.itemRepository.findAllByStatus(status)).size();}

    private List<Vehicle> filterVehicle(List<Item> items)
    {
        List<Vehicle> vehicles = items.stream()
                .filter(i -> i instanceof Vehicle)
                .map(i -> (Vehicle) i)
                .collect(Collectors.toList());

        return vehicles;
    }
}

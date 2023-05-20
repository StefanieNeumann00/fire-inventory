package de.dhbw.fireinventory.domain.status;

import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.location.InternalPlace;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.VehiclePlace;

public class StatusDeterminator {

    public static Status determineVehicleStatus(Location location, Condition condition){
        if(condition == Condition.FUNKTIONSFÄHIG){
            if(location instanceof InternalPlace){
                return Status.EINSATZBEREIT;
            }
            else {return Status.NICHT_VOR_ORT;}
        }
        else if(condition == Condition.NICHT_FUNKTIONSFÄHIG) {return Status.KAPUTT;}
        else {return Status.IN_REPARATUR;}
    }

    public static Status determineEquipmentStatus(Location location, Condition condition){
        if(condition == Condition.FUNKTIONSFÄHIG)
        {
            if(location instanceof VehiclePlace)
            {
                VehiclePlace vehiclePlace = (VehiclePlace) location;
                Status vehicleStatus = vehiclePlace.getVehicle().getStatus();
                Location vehicleLocation = vehiclePlace.getVehicle().getLocation();
                if(vehicleStatus == Status.EINSATZBEREIT){
                    return Status.EINSATZBEREIT;
                }
                else if(vehicleLocation instanceof InternalPlace){
                    return Status.VOR_ORT;
                }
                else {return Status.NICHT_VOR_ORT;}
            }
            else if(location instanceof InternalPlace) {return Status.VOR_ORT;}
            else {return Status.NICHT_VOR_ORT;}
        }
        else if(condition == Condition.NICHT_FUNKTIONSFÄHIG) {return Status.KAPUTT;}
        else {return Status.IN_REPARATUR;}
    }
}

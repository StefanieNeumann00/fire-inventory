package de.dhbw.fireinventory.domain.location;

import de.dhbw.fireinventory.domain.AbstractEntity;
import de.dhbw.fireinventory.domain.place.Place;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "location")
public class Location extends AbstractEntity {

    @ManyToOne()
    @JoinColumn(name = "place_id", referencedColumnName = "id", nullable = true)
    private Place place;

    @ManyToOne()
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = true)
    private Vehicle vehicle;


    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        if (place != null) {
            System.out.println("Setting place ID: " + place.getId().toString());
        } else {
            System.out.println("Setting place ID: null");
        }
        this.place = place;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getDesignation()
    {
        if(this.getPlace() == null)
        {
            if(this.getVehicle() == null)
            {
                return "";
            }
            else
            {
                return this.getVehicle().getDesignation();
            }
        }
        else
        {
            return this.getPlace().getDesignation();
        }
    }
}

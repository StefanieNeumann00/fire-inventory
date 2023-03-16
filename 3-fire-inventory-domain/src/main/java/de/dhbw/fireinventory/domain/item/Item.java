package de.dhbw.fireinventory.domain.item;

import de.dhbw.fireinventory.domain.AbstractEntity;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class Item extends AbstractEntity {

    @ManyToOne()
    @JoinColumn(name = "equipment_id", referencedColumnName = "id", nullable = true)
    private Equipment equipment;

    @ManyToOne()
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = true)
    private Vehicle vehicle;


    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getDesignation()
    {
        if(this.getEquipment() == null)
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
            return this.getEquipment().getDesignation();
        }
    }
}

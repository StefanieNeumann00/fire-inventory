package de.dhbw.plugins.persistence.hibernate.item;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SpringDataItemRepository extends JpaRepository<Item, UUID> {

    @Query("select i from Item i where i.vehicle = :vehicle ")
    Item getItemForVehicle(@Param("vehicle") Vehicle vehicle);

    @Query("select i from Item i where i.equipment = :equipment")
    Item getItemForEquipment(@Param("equipment") Equipment equipment);

}

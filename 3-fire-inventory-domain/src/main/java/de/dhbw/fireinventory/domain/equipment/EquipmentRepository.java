package de.dhbw.fireinventory.domain.equipment;

import java.util.List;

public interface EquipmentRepository {

    List<Equipment> findAllEquipments();

    List<Equipment> findEquipmentWithDesignation(String designation);

    Equipment save(Equipment equipment);

    void delete(Equipment equipment);
}

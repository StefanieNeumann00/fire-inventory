package de.dhbw.fireinventory.domain.equipment;

import de.dhbw.fireinventory.domain.status.Status;

import java.util.List;

public interface EquipmentRepository {

    List<Equipment> findAllBy(String filterText);

    Equipment save(Equipment equipment);

    void delete(Equipment equipment);

    int equipmentStatusCount(String status);
}

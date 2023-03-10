package de.dhbw.fireinventory.domain.equipment;

import java.util.List;

public interface EquipmentRepository {

    List<Equipment> findAllBy(String filterText);

    Equipment save(Equipment equipment);

    void delete(Equipment equipment);
}

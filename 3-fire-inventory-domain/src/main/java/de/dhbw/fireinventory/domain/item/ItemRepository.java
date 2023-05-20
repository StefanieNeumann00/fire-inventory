package de.dhbw.fireinventory.domain.item;

import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.location.Location;

import java.util.List;

public interface ItemRepository {
    List<Item> findAll();

    List<Item> findAllBy(String designation);

    List<Item> findAllByLocation(Location location);

    List<Item> findAllByLocationAndDesignation(Location location, String designation);

    void save(Item item);

    void delete(Item item);
}

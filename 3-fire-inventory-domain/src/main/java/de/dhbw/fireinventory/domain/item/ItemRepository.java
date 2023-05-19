package de.dhbw.fireinventory.domain.item;

import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.location.Location;

import java.util.List;

public interface ItemRepository {
    List<Item> findAll();

    List<Item> findAllBy(String designation);

    List<Item> findAllByLocation(Location location);

    List<Item> findAllByLocationAndDesignation(Location location, String designation);

    List<Item> findAllByLocationAndStatus(Location location, Status status);

    List<Item> findAllByLocationStatusAndDesignation(Location location, Status status, String designation);

    List<Item> findAllByStatus(Status status);

    List<Item> findAllByStatusAndDesignation(Status status, String designation);

    void save(Item item);

    void delete(Item item);
}

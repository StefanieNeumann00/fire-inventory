package de.dhbw.plugins.persistence.hibernate.item;

import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.item.ItemRepository;
import de.dhbw.fireinventory.domain.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepositoryBridge implements ItemRepository {

    private final SpringDataItemRepository springDataItemRepository;

    @Autowired
    public ItemRepositoryBridge(final SpringDataItemRepository springDataItemRepository) {
        this.springDataItemRepository = springDataItemRepository;
    }

    @Override
    public List<Item> findAll() {
        return this.springDataItemRepository.findAll();
    }

    @Override
    public List<Item> findAllBy(String designation) {
        return this.springDataItemRepository.findAllBy(designation);
    }

    @Override
    public List<Item> findAllByLocation(Location location) {
        return this.springDataItemRepository.findAllByLocation(location);
    }

    @Override
    public List<Item> findAllByLocationAndDesignation(Location location, String designation) {
        return this.springDataItemRepository.findAllByLocationAndDesignation(location, designation);
    }

    @Override
    public List<Item> findAllByLocationAndStatus(Location location, Status status) {
        return this.springDataItemRepository.findAllByLocationAndStatus(location, status);
    }

    @Override
    public List<Item> findAllByLocationStatusAndDesignation(Location location, Status status, String designation) {
        return this.springDataItemRepository.findAllByLocationStatusAndDesignation(location, status, designation);
    }

    @Override
    public List<Item> findAllByStatus(Status status) {
        return this.springDataItemRepository.findAllByStatus(status);
    }

    @Override
    public List<Item> findAllByStatusAndDesignation(Status status, String designation) {
        return this.springDataItemRepository.findAllByStatusAndDesignation(status, designation);
    }

    @Override
    public void save(Item item) {
        this.springDataItemRepository.save(item);
    }

    @Override
    public void delete(Item item) {
        this.springDataItemRepository.delete(item);
    }
}

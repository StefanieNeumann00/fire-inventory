package de.dhbw.fireinventory.application.item;

import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.item.ItemRepository;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.location.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemApplicationService {

    private final ItemRepository itemRepository;
    @Autowired
    public ItemApplicationService(final ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findAllItems(){ return this.itemRepository.findAll(); }
}

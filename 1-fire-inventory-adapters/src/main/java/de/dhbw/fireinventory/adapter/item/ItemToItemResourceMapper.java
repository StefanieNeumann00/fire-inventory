package de.dhbw.fireinventory.adapter.item;

import de.dhbw.fireinventory.domain.item.Item;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ItemToItemResourceMapper implements Function<Item, ItemResource> {

    @Override
    public ItemResource apply(final Item item) {
        return map(item);
    }

    private ItemResource map(final Item item) {
        return new ItemResource(item.getDesignation(), item.getVehicle(), item.getEquipment());
    }
}

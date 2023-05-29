package de.dhbw.fireinventory.adapter.application.item;

import de.dhbw.fireinventory.application.domain.service.item.ItemResource;

import java.util.List;

public interface ItemApplicationAdapter {

    List<ItemResource> findAllItems();
}

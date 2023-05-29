package de.dhbw.fireinventory.adapter.application.item;

import de.dhbw.fireinventory.adapter.mapper.item.ItemToItemResourceMapper;
import de.dhbw.fireinventory.application.domain.service.item.ItemDomainServicePort;
import de.dhbw.fireinventory.application.domain.service.item.ItemResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemAppAdapter implements ItemApplicationAdapter {

    private final ItemDomainServicePort itemDomainServicePort;
    private final ItemToItemResourceMapper itemToItemResourceMapper;

    @Override
    public List<ItemResource> findAllItems() {
        return this.itemDomainServicePort.findAllItems().stream()
                .map(itemToItemResourceMapper)
                .collect(Collectors.toList());
    }
}

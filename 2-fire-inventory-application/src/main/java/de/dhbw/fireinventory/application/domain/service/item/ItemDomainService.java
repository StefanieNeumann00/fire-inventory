package de.dhbw.fireinventory.application.domain.service.item;

import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.item.Item;

public interface ItemDomainService extends ItemDomainServicePort {

    void save(Item item, Condition condition);

    void delete(Item item);

    boolean hasLinkedItems(Item item);


}

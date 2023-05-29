package de.dhbw.fireinventory.application.mediator.service.item;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.item.Item;

public interface ItemServicePort {

    boolean delete(Item item);

    void save(Item item, Condition condition);
}

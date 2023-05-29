package de.dhbw.fireinventory.application.mediator.colleague;

import de.dhbw.fireinventory.application.ConcreteApplicationMediator;
import de.dhbw.fireinventory.application.domain.service.item.ItemDomainService;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.core.annotation.Order;

public class ItemColleague extends Colleague{

    private final ItemDomainService itemDomainService;

    protected ItemColleague(final ConcreteApplicationMediator mediator, final ItemDomainService itemDomainService) {
        super(mediator, 1);
        this.itemDomainService = itemDomainService;
    }

    @Override
    public void onDeleteVehicle(Item item) throws HasLinkedException {
        if (item instanceof Vehicle && !itemDomainService.hasLinkedItems(item)) {
            itemDomainService.delete(item);
        }
        else throw new HasLinkedException();
    }

    @Override
    public void onSaveVehicle(Item item, Condition condition) {
        itemDomainService.save(item, condition);
    }
}

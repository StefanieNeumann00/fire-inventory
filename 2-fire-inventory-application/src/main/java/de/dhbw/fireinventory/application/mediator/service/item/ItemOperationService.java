package de.dhbw.fireinventory.application.mediator.service.item;

import de.dhbw.fireinventory.application.ConcreteApplicationMediator;
import de.dhbw.fireinventory.application.domain.service.item.ItemDomainService;
import de.dhbw.fireinventory.application.mediator.colleague.HasLinkedException;
import de.dhbw.fireinventory.application.mediator.colleague.ItemColleague;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.item.Item;
import org.springframework.stereotype.Service;

@Service
public class ItemOperationService extends ItemColleague implements ItemService {

    private final ItemDomainService itemDomainService;

    public ItemOperationService(ConcreteApplicationMediator mediator, ItemDomainService itemDomainService) {
        super(mediator, itemDomainService);
        this.itemDomainService = itemDomainService;
    }

    public boolean delete(Item item) {
        try {
            onDeleteVehicle(item);
            getMediator().onDeleteVehicle(item, this);
        }
        catch (HasLinkedException e) {
            return false;
        }
        return true;
    }

    public void save(Item item, Condition condition) {
        onSaveVehicle(item, condition);
        getMediator().onSaveVehicle(item, condition, this);
    }
}

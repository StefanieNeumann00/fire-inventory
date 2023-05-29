package de.dhbw.fireinventory.application.mediator.colleague;

import de.dhbw.fireinventory.application.ConcreteApplicationMediator;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class Colleague {

    @Getter(AccessLevel.PROTECTED)
    private final ConcreteApplicationMediator mediator;

    protected Colleague(final ConcreteApplicationMediator mediator, int priority) {
        this.mediator = mediator;
        this.mediator.addColleague(this, priority);
    }

    public void onDeleteLocation(Location location) throws HasLinkedException {

    }

    public void onSaveVehicle(Item item, Condition condition) {

    }

    public void onDeleteVehicle(Item item) throws HasLinkedException {

    }
}

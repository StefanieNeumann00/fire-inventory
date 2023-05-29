package de.dhbw.fireinventory.application;

import de.dhbw.fireinventory.application.mediator.colleague.Colleague;
import de.dhbw.fireinventory.application.mediator.colleague.HasLinkedException;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;

public interface Mediator {

    void onDeleteLocation(Location location, Colleague colleague) throws HasLinkedException;

    void onSaveVehicle(Item item, Condition condition, Colleague colleague);

    void onDeleteVehicle(Item item, Colleague colleague) throws HasLinkedException;

}

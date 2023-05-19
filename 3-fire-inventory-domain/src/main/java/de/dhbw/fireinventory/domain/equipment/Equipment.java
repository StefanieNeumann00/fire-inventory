package de.dhbw.fireinventory.domain.equipment;

import de.dhbw.fireinventory.domain.item.Item;

import javax.persistence.*;

@Entity
@Table(name = "equipment")
public class Equipment extends Item {
}

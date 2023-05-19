package de.dhbw.fireinventory.domain.vehicle;

import de.dhbw.fireinventory.domain.item.Item;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "vehicle")
public class Vehicle extends Item {
}



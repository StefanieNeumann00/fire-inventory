package de.dhbw.fireinventory.domain.location;

import de.dhbw.fireinventory.domain.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Location extends AbstractEntity {

}

package de.dhbw.fireinventory.domain.location;

import de.dhbw.fireinventory.domain.AbstractEntity;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "location")
@DiscriminatorColumn(name="location_type",
        discriminatorType = DiscriminatorType.STRING)
public abstract class Location extends AbstractEntity {

}

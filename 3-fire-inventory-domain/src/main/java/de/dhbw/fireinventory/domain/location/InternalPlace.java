package de.dhbw.fireinventory.domain.location;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("InternalPlace")
public class InternalPlace extends Location {
}

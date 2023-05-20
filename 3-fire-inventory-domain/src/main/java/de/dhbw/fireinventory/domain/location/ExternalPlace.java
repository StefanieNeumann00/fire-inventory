package de.dhbw.fireinventory.domain.location;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ExternalPlace")
public class ExternalPlace extends Location {
}

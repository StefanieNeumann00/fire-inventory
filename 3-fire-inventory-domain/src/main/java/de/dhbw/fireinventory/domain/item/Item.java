package de.dhbw.fireinventory.domain.item;

import de.dhbw.fireinventory.domain.AbstractEntity;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.condition.ConditionConverter;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.location.Location;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "item")
@DiscriminatorColumn(name="item_type",
        discriminatorType = DiscriminatorType.STRING)
public abstract class Item extends AbstractEntity {

    @ManyToOne
    @NotNull
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @NotNull
    @Convert(converter = ConditionConverter.class)
    protected Condition condition;

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public Condition getCondition() {
        return this.condition;
    }

    public void setCondition(Condition condition){
        this.condition = condition;
    }

    public Status getStatus() {return this.determineStatus(this.condition);}

    public abstract Status determineStatus(Condition condition);
}

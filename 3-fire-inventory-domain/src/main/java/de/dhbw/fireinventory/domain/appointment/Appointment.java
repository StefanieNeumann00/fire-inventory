package de.dhbw.fireinventory.domain.appointment;

import de.dhbw.fireinventory.domain.AbstractEntity;
import de.dhbw.fireinventory.domain.item.Item;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "appointment")
public class Appointment extends AbstractEntity {

    @ManyToOne
    @NotNull
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @NotNull
    private LocalDate dueDate;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDate getDueDate() { return dueDate; }

    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}

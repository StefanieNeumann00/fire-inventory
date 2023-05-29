package de.dhbw.plugins.persistence.hibernate.item;

import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataItemRepository extends JpaRepository<Item, UUID> {

    @Query("select i from Item i where lower(i.designation) like lower(concat('%', :designation, '%'))")
    List<Item> findAllBy(@Param("designation") String designation);

    @Query("select i from Item i where i.location = :location")
    List<Item> findAllByLocation(@Param("location") Location location);

    @Query("select i from Item i where i.location = :location and lower(i.designation) like lower(concat('%', :designation, '%'))")
    List<Item> findAllByLocationAndDesignation(@Param("location") Location location, @Param("designation") String designation);

    @Query(value = "select case when exists (select * from Item where location =" +
            "(select * from Location where Location.vehicle = :vehicle) then cast (1 as bit) else cast (0 as bit) end", nativeQuery = true)
    boolean hasLinkedItems(@Param("vehicle") Vehicle vehicle);

    @Query(value = "select case when exists (select * from Item where id = :itemId) then cast (1 as bit) else cast (0 as bit) end", nativeQuery = true)
    boolean isPresent(@Param("itemId") UUID itemId);
}

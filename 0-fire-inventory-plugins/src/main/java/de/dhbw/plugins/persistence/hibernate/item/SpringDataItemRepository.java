package de.dhbw.plugins.persistence.hibernate.item;

import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.location.Location;
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

    @Query("select i from Item i where i.location = :location and i.status = :status")
    List<Item> findAllByLocationAndStatus(@Param("location") Location location, @Param("status") Status status);

    @Query("select i from Item i where i.location = :location and i.status = :status and lower(i.designation) like lower(concat('%', :designation, '%'))")
    List<Item> findAllByLocationStatusAndDesignation(@Param("location") Location location, @Param("status") Status status, @Param("designation") String designation);

    @Query("select i from Item i where i.status = :status")
    List<Item> findAllByStatus(@Param("status") Status status);

    @Query("select i from Item i where i.status = :status and lower(i.designation) like lower(concat('%', :designation, '%'))")
    List<Item> findAllByStatusAndDesignation(@Param("status") Status status, @Param("designation") String designation);
}

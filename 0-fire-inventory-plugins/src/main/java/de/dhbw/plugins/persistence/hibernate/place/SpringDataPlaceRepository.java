package de.dhbw.plugins.persistence.hibernate.place;

import de.dhbw.fireinventory.domain.equipment.Equipment;
import de.dhbw.fireinventory.domain.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataPlaceRepository extends JpaRepository<Place, UUID> {

    @Query("select p from Place p where lower(p.designation) like lower(concat('%', :designation, '%'))")
    List<Place> findAllBy(@Param("designation") String designation);
}

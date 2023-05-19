package de.dhbw.plugins.persistence.hibernate.location;

import de.dhbw.fireinventory.domain.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataLocationRepository extends JpaRepository<Location, UUID> {

    @Query("select l from Location l where lower(l.designation) like lower(concat('%', :designation, '%'))")
    List<Location> findAllBy(@Param("designation") String designation);
}

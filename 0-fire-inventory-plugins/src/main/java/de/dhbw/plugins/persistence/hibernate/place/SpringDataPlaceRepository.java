package de.dhbw.plugins.persistence.hibernate.place;

import de.dhbw.fireinventory.domain.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataPlaceRepository extends JpaRepository<Place, UUID> {
}

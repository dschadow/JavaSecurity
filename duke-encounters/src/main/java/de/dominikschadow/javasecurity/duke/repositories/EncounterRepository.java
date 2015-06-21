package de.dominikschadow.javasecurity.duke.repositories;

import de.dominikschadow.javasecurity.duke.domain.Encounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncounterRepository extends JpaRepository<Encounter, Long> {
}

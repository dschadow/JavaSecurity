package de.dominikschadow.javasecurity.duke.repositories;

import de.dominikschadow.javasecurity.duke.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

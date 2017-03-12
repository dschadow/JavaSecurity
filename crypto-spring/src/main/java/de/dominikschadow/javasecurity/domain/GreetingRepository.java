package de.dominikschadow.javasecurity.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by dos on 12.03.17.
 */
public interface GreetingRepository extends JpaRepository<Greeting, Integer>, JpaSpecificationExecutor {
}

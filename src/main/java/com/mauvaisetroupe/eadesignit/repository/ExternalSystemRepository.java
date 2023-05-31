package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.ExternalSystem;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExternalSystem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExternalSystemRepository extends JpaRepository<ExternalSystem, Long> {
    Optional<ExternalSystem> findByExternalSystemID(String id);
}

package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.ExternalReference;
import com.mauvaisetroupe.eadesignit.domain.ExternalSystem;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExternalReference entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExternalReferenceRepository extends JpaRepository<ExternalReference, Long> {
    Optional<ExternalReference> findByExternalSystemAndExternalID(ExternalSystem externalSystem, String id);
}

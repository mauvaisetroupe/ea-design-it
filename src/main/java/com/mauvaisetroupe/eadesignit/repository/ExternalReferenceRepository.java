package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.ExternalReference;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExternalReference entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExternalReferenceRepository extends JpaRepository<ExternalReference, Long> {}

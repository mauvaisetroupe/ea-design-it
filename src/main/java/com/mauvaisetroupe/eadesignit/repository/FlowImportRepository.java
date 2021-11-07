package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.FlowImport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FlowImport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlowImportRepository extends JpaRepository<FlowImport, Long> {}

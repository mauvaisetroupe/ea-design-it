package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.DataFlowImport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DataFlowImport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataFlowImportRepository extends JpaRepository<DataFlowImport, Long> {}

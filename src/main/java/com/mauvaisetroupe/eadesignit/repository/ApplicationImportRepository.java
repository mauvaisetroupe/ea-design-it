package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApplicationImport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationImportRepository extends JpaRepository<ApplicationImport, Long> {}

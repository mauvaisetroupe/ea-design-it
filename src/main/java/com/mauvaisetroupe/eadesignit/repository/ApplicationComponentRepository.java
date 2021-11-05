package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApplicationComponent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationComponentRepository extends JpaRepository<ApplicationComponent, Long> {}

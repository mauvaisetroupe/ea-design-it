package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.OrganizationalEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrganizationalEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationalEntityRepository extends JpaRepository<OrganizationalEntity, Long> {}

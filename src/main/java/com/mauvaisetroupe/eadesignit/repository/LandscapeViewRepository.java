package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LandscapeView entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LandscapeViewRepository extends JpaRepository<LandscapeView, Long> {
    LandscapeView findByDiagramName(String diagramName);
}

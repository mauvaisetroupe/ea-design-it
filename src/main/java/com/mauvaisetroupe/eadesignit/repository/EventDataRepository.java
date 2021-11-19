package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.EventData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EventData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventDataRepository extends JpaRepository<EventData, Long> {}

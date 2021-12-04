package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.DataFlowItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DataFlowItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataFlowItemRepository extends JpaRepository<DataFlowItem, Long> {}

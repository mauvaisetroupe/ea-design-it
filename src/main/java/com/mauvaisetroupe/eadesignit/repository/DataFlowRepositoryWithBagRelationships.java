package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DataFlowRepositoryWithBagRelationships {
    Optional<DataFlow> fetchBagRelationships(Optional<DataFlow> dataFlow);

    List<DataFlow> fetchBagRelationships(List<DataFlow> dataFlows);

    Page<DataFlow> fetchBagRelationships(Page<DataFlow> dataFlows);
}

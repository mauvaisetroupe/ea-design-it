package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.DataObject;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DataObjectRepositoryWithBagRelationships {
    Optional<DataObject> fetchBagRelationships(Optional<DataObject> dataObject);

    List<DataObject> fetchBagRelationships(List<DataObject> dataObjects);

    Page<DataObject> fetchBagRelationships(Page<DataObject> dataObjects);
}

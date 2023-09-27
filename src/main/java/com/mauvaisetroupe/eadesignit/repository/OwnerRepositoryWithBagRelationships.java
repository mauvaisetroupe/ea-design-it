package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.Owner;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface OwnerRepositoryWithBagRelationships {
    Optional<Owner> fetchBagRelationships(Optional<Owner> owner);

    List<Owner> fetchBagRelationships(List<Owner> owners);

    Page<Owner> fetchBagRelationships(Page<Owner> owners);
}

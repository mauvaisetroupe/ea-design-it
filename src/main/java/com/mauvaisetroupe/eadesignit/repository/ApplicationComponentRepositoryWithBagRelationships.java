package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ApplicationComponentRepositoryWithBagRelationships {
    Optional<ApplicationComponent> fetchBagRelationships(Optional<ApplicationComponent> applicationComponent);

    List<ApplicationComponent> fetchBagRelationships(List<ApplicationComponent> applicationComponents);

    Page<ApplicationComponent> fetchBagRelationships(Page<ApplicationComponent> applicationComponents);
}

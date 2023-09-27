package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface LandscapeViewRepositoryWithBagRelationships {
    Optional<LandscapeView> fetchBagRelationships(Optional<LandscapeView> landscapeView);

    List<LandscapeView> fetchBagRelationships(List<LandscapeView> landscapeViews);

    Page<LandscapeView> fetchBagRelationships(Page<LandscapeView> landscapeViews);
}

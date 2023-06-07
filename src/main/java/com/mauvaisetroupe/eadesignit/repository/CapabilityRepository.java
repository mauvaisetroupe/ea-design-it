package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Capability entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CapabilityRepository extends JpaRepository<Capability, Long> {
    List<Capability> findByNameIgnoreCaseAndLevel(String name, Integer level);
    List<Capability> findByNameIgnoreCaseAndParentNameIgnoreCaseAndLevel(String name, String parent, Integer level);
    void deleteByApplicationsIsEmpty();
    List<Capability> findByLevel(int i);
}

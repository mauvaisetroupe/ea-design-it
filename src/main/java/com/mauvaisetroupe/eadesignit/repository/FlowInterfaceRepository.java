package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityResult;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.validation.constraints.NotNull;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.Interface;
import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FlowInterface entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlowInterfaceRepository extends JpaRepository<FlowInterface, Long> {
    Optional<FlowInterface> findByAlias(String idFlowFromExcel);
    Set<FlowInterface> findBySource_NameOrTarget_Name(String sourceName, String targetName);
    Set<FlowInterface> findByAliasIn(@NotNull List<String> aliasToMerge);

    @Query(
        value = "select i1.id from FLOW_INTERFACE i1 " +
        "JOIN (select distinct source_id, target_id, protocol_id from FLOW_INTERFACE  group by source_id, target_id, protocol_id HAVING count(*) > 1) i2 " +
        "left join APPLICATION app1 on i1.SOURCE_ID = app1.ID " +
        "left join APPLICATION app2 on i1.TARGET_ID = app2.ID " +
        "left join PROTOCOL on i1.PROTOCOL_ID = PROTOCOL.ID " +
        "WHERE i1.source_id=i2.source_id AND i1.target_id=i2.target_id AND i1.protocol_id=i2.protocol_id " +
        "ORDER BY app1.name, app2.name, protocol.name ",
        nativeQuery = true
    )
    public List<Long> getDuplicatedInterfaceIds();

    @Query(
        value = "select i1.alias, app1.name as source, app2.name as target from FLOW_INTERFACE i1 " +
        "JOIN (select distinct source_id, target_id, protocol_id from FLOW_INTERFACE  group by source_id, target_id, protocol_id HAVING count(*) > 1) i2 " +
        "left join APPLICATION app1 on i1.SOURCE_ID = app1.ID " +
        "left join APPLICATION app2 on i1.TARGET_ID = app2.ID " +
        "left join PROTOCOL on i1.PROTOCOL_ID = PROTOCOL.ID " +
        "WHERE i1.source_id=i2.source_id AND i1.target_id=i2.target_id AND i1.protocol_id=i2.protocol_id " +
        "ORDER BY app1.name, app2.name, protocol.name ",
        nativeQuery = true
    )
    public List<Object[]> getDuplicatedInterfaceAsObject();

    @Query(
        value = "select i1.* from FLOW_INTERFACE i1  " +
        "JOIN (select distinct source_id, target_id, protocol_id from FLOW_INTERFACE  group by source_id, target_id, protocol_id HAVING count(*) > 1) i2  " +
        "on i1.source_id=i2.source_id AND i1.target_id=i2.target_id AND i1.protocol_id=i2.protocol_id  " +
        "left join APPLICATION app1 on i1.SOURCE_ID = app1.ID  " +
        "left join APPLICATION app2 on i1.TARGET_ID = app2.ID  " +
        "left join PROTOCOL on i1.PROTOCOL_ID = PROTOCOL.ID  " +
        "ORDER BY app1.name, app2.name, protocol.name ",
        nativeQuery = true
    )
    public List<FlowInterface> getDuplicatedInterface();
}

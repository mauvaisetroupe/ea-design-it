package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import javax.persistence.EntityResult;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.validation.constraints.NotNull;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.Interface;
import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FlowInterface entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlowInterfaceRepository extends JpaRepository<FlowInterface, Long> {
    Optional<FlowInterface> findByAlias(String idFlowFromExcel);
    SortedSet<FlowInterface> findByAliasIn(@NotNull List<String> aliasToMerge);

    @Query(
        value = "select i1.* from INTERFACE i1  " +
        "JOIN (select distinct source_id, target_id, protocol_id from INTERFACE  group by source_id, target_id, protocol_id HAVING count(*) > 1) i2  " +
        "on i1.source_id=i2.source_id AND i1.target_id=i2.target_id AND i1.protocol_id=i2.protocol_id  " +
        "left join APPLICATION app1 on i1.SOURCE_ID = app1.ID  " +
        "left join APPLICATION app2 on i1.TARGET_ID = app2.ID  " +
        "left join PROTOCOL on i1.PROTOCOL_ID = PROTOCOL.ID  " +
        "ORDER BY app1.name, app2.name, protocol.name ",
        nativeQuery = true
    )
    public List<FlowInterface> getDuplicatedInterface();

    @Query(value = "select distinct(i.alias) from FlowInterface i")
    public List<String> findAlias();

    @Query(
        value = "select i from FlowInterface i" +
        " left join fetch i.source" +
        " left join fetch i.target" +
        " left join fetch i.sourceComponent" +
        " left join fetch i.protocol" +
        " left join fetch i.targetComponent" +
        " left join fetch i.owner"
    )
    public List<FlowInterface> findAll();

    @Query(
        value = "select i from FlowInterface i" +
        " left join fetch i.source" +
        " left join fetch i.target" +
        " left join fetch i.sourceComponent" +
        " left join fetch i.protocol" +
        " left join fetch i.targetComponent " +
        " left join fetch i.owner" +
        " left join fetch i.dataFlows" +
        " where i.id=:id"
    )
    Optional<FlowInterface> findById(@Param("id") Long id);

    @Query(
        value = "select i from FlowInterface i" +
        " left join fetch i.source" +
        " left join fetch i.target" +
        " left join fetch i.sourceComponent" +
        " left join fetch i.targetComponent " +
        " left join fetch i.protocol" +
        " left join fetch i.owner" +
        " left join fetch i.dataFlows" +
        " where i.source.id=:sourceId and i.target.id=:targetId and i.protocol.id=:protocolId"
    )
    public List<FlowInterface> findBySourceIdAndTargetIdAndProtocolId(
        @Param("sourceId") Long sourceId,
        @Param("targetId") Long targetId,
        @Param("protocolId") Long protocolId
    );

    @Query(
        value = "select i from FlowInterface i" +
        " left join fetch i.source" +
        " left join fetch i.target" +
        " left join fetch i.sourceComponent" +
        " left join fetch i.targetComponent " +
        " left join fetch i.protocol" +
        " left join fetch i.owner" +
        " left join fetch i.dataFlows" +
        " where i.source.id=:sourceId and i.target.id=:targetId"
    )
    public List<FlowInterface> findBySourceIdAndTargetId(@Param("sourceId") Long sourceId, @Param("targetId") Long targetId);

    public SortedSet<FlowInterface> findBySource_NameOrTarget_Name(String sourceName, String targetName);

    public SortedSet<FlowInterface> findBySourceIdInAndTargetIdIn(Long[] sourceIds, Long[] targetIds);
}

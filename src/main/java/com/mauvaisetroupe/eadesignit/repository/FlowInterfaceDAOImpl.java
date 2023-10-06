package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import com.mauvaisetroupe.eadesignit.domain.Protocol;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ProtocolType;
import com.mauvaisetroupe.eadesignit.repository.view.FlowInterfaceLight;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class FlowInterfaceDAOImpl implements FlowInterfaceDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FlowInterfaceDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<FlowInterfaceLight> findAllInterfaceLight() {
        String select =
            "select " +
            " i1.*, " +
            " s.id as s_id, s.alias as s_alias, s.name as s_name , " +
            " t.id as t_id, t.alias as t_alias, t.name as t_name ," +
            " sc.id as sc_id, sc.alias as sc_alias, sc.name as sc_name ," +
            " tc.id as tc_id, tc.alias as tc_alias, tc.name as tc_name, " +
            " p.id as p_id, p.name as p_name, p.jhi_type as p_type " +
            " from INTERFACE i1  " +
            " left join APPLICATION s on i1.SOURCE_ID = s.ID  " +
            " left join APPLICATION t on i1.TARGET_ID = t.ID  " +
            " left join COMPONENT sc on i1.SOURCE_COMPONENT_ID = sc.ID  " +
            " left join COMPONENT tc on i1.TARGET_COMPONENT_ID = tc.ID  " +
            " left join PROTOCOL p on i1.PROTOCOL_ID = p.ID  ";

        List<FlowInterfaceLight> list = jdbcTemplate.query(
            select,
            new RowMapper<FlowInterfaceLight>() {
                @Override
                public FlowInterfaceLight mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Protocol protocol = null;
                    if (rs.getLong("p_id") > 0) {
                        protocol = new Protocol();
                        protocol.setId(rs.getLong("p_id"));
                        protocol.setName(rs.getString("p_name"));
                        protocol.setType(rs.getString("p_type") != null ? ProtocolType.valueOf(rs.getString("p_type")) : null);
                    }

                    Application source = null;
                    if (rs.getLong("s_id") > 0) {
                        source = new Application();
                        source.setId(rs.getLong("s_id"));
                        source.setAlias(rs.getString("s_alias"));
                        source.setName(rs.getString("s_name"));
                    }

                    Application target = null;
                    if (rs.getLong("t_id") > 0) {
                        target = new Application();
                        target.setId(rs.getLong("t_id"));
                        target.setAlias(rs.getString("t_alias"));
                        target.setName(rs.getString("t_name"));
                    }

                    ApplicationComponent sourceComponent = null;
                    if (rs.getLong("sc_id") > 0) {
                        sourceComponent = new ApplicationComponent();
                        sourceComponent.setId(rs.getLong("sc_id"));
                        sourceComponent.setAlias(rs.getString("sc_alias"));
                        sourceComponent.setName(rs.getString("sc_name"));
                    }

                    ApplicationComponent targetComponent = null;
                    if (rs.getLong("tc_id") > 0) {
                        targetComponent = new ApplicationComponent();
                        targetComponent.setId(rs.getLong("tc_id"));
                        targetComponent.setAlias(rs.getString("tc_alias"));
                        targetComponent.setName(rs.getString("tc_name"));
                    }

                    FlowInterfaceLight flowInterface = new FlowInterfaceLight(
                        rs.getLong("id"),
                        rs.getString("alias"),
                        source,
                        target,
                        sourceComponent,
                        targetComponent,
                        protocol
                    );
                    return flowInterface;
                }
            }
        );
        return list;
    }
}

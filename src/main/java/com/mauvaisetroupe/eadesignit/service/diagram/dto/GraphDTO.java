package com.mauvaisetroupe.eadesignit.service.diagram.dto;

import com.mauvaisetroupe.eadesignit.domain.FlowGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class GraphDTO {

    private Map<Long, Application> applications = new LinkedHashMap<>();
    // groupName : List<appli>
    private Map<String, List<Application>> applicationGroups = new HashMap<>();
    // idGroup : List<edge>
    private Map<Long, EdgeGroup> edgesGroups = new HashMap<>();

    // A - alias1 -> B
    // A - alias2 -> B
    // B - alias3 -> A
    private List<Edge> edges = new ArrayList<>();

    // A - alias1, alias 2 -> B
    // B - alias3 -> A
    //private List<Edge> consolidatedEdges = new ArrayList<>();
    private Map<String, Edge> consolidatedMap = new HashMap<>();

    // A <- alias1, alias 2, alias 3 -> B
    //private List<Edge> bidirectionalConsolidatedEdges = new ArrayList<>();
    private Map<String, Edge> bidirectionalConsolidatedMap = new HashMap<>();

    public void consolidateEdges() {
        for (Edge edge : this.edges) {
            String key = getKey(edge);
            Edge _edge = consolidatedMap.get(key);
            if (_edge == null) {
                _edge = new Edge(key, edge.getSourceId(), edge.getTargetId(), edge.getLabels(), edge.isBidirectional());
                this.consolidatedMap.put(key, _edge);
            } else {
                _edge.addLabel(edge.getLabels());
            }
            //this.consolidatedEdges = new ArrayList<>(consolidatedMap.values());

            String consolidatedKey = getConsolidatedKey(edge);
            _edge = bidirectionalConsolidatedMap.get(consolidatedKey);
            if (_edge == null) {
                _edge = new Edge(consolidatedKey, edge.getSourceId(), edge.getTargetId(), edge.getLabels(), edge.isBidirectional());
                this.bidirectionalConsolidatedMap.put(consolidatedKey, _edge);
            } else {
                _edge.addLabel(edge.getLabels());
                if (!_edge.getSourceId().equals(edge.getSourceId())) {
                    _edge.setBidirectional(true);
                }
            }
            //this.bidirectionalConsolidatedEdges = new ArrayList<>(bidirectionalConsolidatedMap.values());
        }
    }

    // Getters & Setters

    private String getKey(Edge edge) {
        return edge.getSourceId() + "-" + edge.getTargetId();
    }

    private String getConsolidatedKey(Edge edge) {
        return Math.min(edge.getSourceId(), edge.getTargetId()) + "-" + Math.max(edge.getSourceId(), edge.getTargetId());
    }

    private String getConsolidatedKey(Long id, Long id2) {
        return Math.min(id, id2) + "-" + Math.max(id, id2);
    }

    public Collection<Application> getApplications() {
        return applications.values();
    }

    public Application getApplication(Long id) {
        return applications.get(id);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public Collection<Edge> getConsolidatedEdges() {
        return consolidatedMap.values();
    }

    public Collection<Edge> getBidirectionalConsolidatedEdges() {
        return bidirectionalConsolidatedMap.values();
    }

    public Map<String, List<Application>> getApplicationGroups() {
        return applicationGroups;
    }

    public Map<Long, EdgeGroup> getEdgesGroups() {
        return edgesGroups;
    }

    public Collection<Application> getApplicationsWithoutGroups() {
        List<Application> tmp = new ArrayList<>(this.applications.values());
        for (List<Application> tmp2 : this.applicationGroups.values()) {
            tmp.removeAll(tmp2);
        }
        return tmp;
    }

    // Helpers

    public void addApplication(Application application) {
        this.applications.put(application.getId(), application);
    }

    public void addApplicationIngroup(String groupName, Application application) {
        List<Application> applicationsInGroup = new ArrayList<>();
        if (this.applicationGroups.containsKey(groupName)) {
            applicationsInGroup = this.applicationGroups.get(groupName);
        }
        applicationsInGroup.add(application);
        this.applicationGroups.put(groupName, applicationsInGroup);
    }

    public void addEdgeIngroup(FlowGroup flowGroup, String title, String url) {
        if (!this.edgesGroups.containsKey(flowGroup.getId())) {
            List<Edge> edgesInGroup = new ArrayList<>();
            flowGroup
                .getSteps()
                .stream()
                .forEach(f -> {
                    Edge tmpEdge = getEdge(f.getFlow().getId() + "-" + f.getStepOrder());
                    if (tmpEdge != null) {
                        edgesInGroup.add(tmpEdge);
                    }
                });
            this.edgesGroups.put(flowGroup.getId(), new EdgeGroup(edgesInGroup, title, url));
        }
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public boolean containsApplication(Long id) {
        return applications.containsKey(id);
    }

    public boolean containsEdge(Long id, Long id2, boolean checkDirection) {
        // If consolidatedMap contains interfaces applications ID, that means that a edge exist
        // (for this interface or another with same source or target)
        Edge _edge = this.bidirectionalConsolidatedMap.get(getConsolidatedKey(id, id2));
        if (!checkDirection) {
            return _edge != null;
        } else {
            return (_edge != null && (_edge.getSourceId().equals(id) || _edge.isBidirectional()));
        }
    }

    /**
     * @param edgeId
     * @return
     */
    public Edge getEdge(String edgeId) {
        for (Edge edge : this.getEdges()) {
            if (edge.getId().equals(edgeId)) {
                return edge;
            }
        }
        return null;
    }

    public Edge getBidirectionalConsolidatedEdge(Edge edge) {
        Edge _edge = bidirectionalConsolidatedMap.get(getConsolidatedKey(edge));
        return _edge;
    }

    public int getNbConnection(Application application) {
        Set<String> connections = new HashSet<>();
        for (Edge edge : edges) {
            if (!edge.getSourceId().equals(edge.getTargetId())) {
                if (edge.getSourceId().equals(application.getId())) {
                    connections.add("OUT_" + edge.getTargetId());
                }
                if (edge.getTargetId().equals(application.getId())) {
                    connections.add("IN_" + edge.getSourceId());
                }
            }
        }
        return connections.size();
    }

    public EdgeGroup isStartingGroup(Edge edge) {
        for (Entry<Long, EdgeGroup> _edge : this.getEdgesGroups().entrySet()) {
            if (_edge.getValue().getEdges().get(0).getId().equals(edge.getId())) return _edge.getValue();
        }
        return null;
    }

    public EdgeGroup isEndingGroup(Edge edge) {
        for (Entry<Long, EdgeGroup> _edge : this.getEdgesGroups().entrySet()) {
            int lastIndex = _edge.getValue().getEdges().size() - 1;
            if (_edge.getValue().getEdges().get(lastIndex).getId().equals(edge.getId())) return _edge.getValue();
        }
        return null;
    }
}

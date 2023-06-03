package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.repository.view.FlowInterfaceLight;
import java.util.List;

public interface FlowInterfaceDAO {
    List<FlowInterfaceLight> findAllInterfaceLight();
}

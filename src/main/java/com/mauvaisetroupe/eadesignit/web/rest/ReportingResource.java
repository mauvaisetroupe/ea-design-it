package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.service.reporting.ReportingService;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
@Transactional
public class ReportingResource {

    @Autowired
    private FlowInterfaceRepository flowInterfaceRepository;

    @Autowired
    private ReportingService reportingService;

    @GetMapping(value = "reporting/duplicate-interface/")
    public List<FlowInterface> getDuplicatedInterfaces() {
        return flowInterfaceRepository.getDuplicatedInterface();
    }

    @PostMapping(value = "reporting/merge-duplicate-interface/{id}")
    public ResponseEntity<Void> mergeInterfaces(@PathVariable Long id, @NotNull @RequestBody List<String> aliasToMerge) {
        reportingService.mergeInterfaces(id, aliasToMerge);
        return ResponseEntity.noContent().build();
    }
}

package com.biteco.reports.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biteco.reports.dto.CreateReportRequest;
import com.biteco.reports.dto.ReportResponse;
import com.biteco.reports.model.CostReport;
import com.biteco.reports.repository.CostReportRepository;
import com.biteco.reports.security.EncryptionService;

@Service
public class CostReportService {

    private final CostReportRepository repository;
    private final EncryptionService encryptionService;
    private final AwsCostService awsCostService;

    public CostReportService(CostReportRepository repository, 
                             EncryptionService encryptionService,
                             AwsCostService awsCostService) {
        this.repository = repository;
        this.encryptionService = encryptionService;
        this.awsCostService = awsCostService;
    }

    @Transactional
    public ReportResponse createReport(CreateReportRequest req) {
        Map<String, Object> awsData;
        
        if (req.getStartDate() != null && req.getEndDate() != null) {
            awsData = awsCostService.getAwsCosts(req.getCompanyId(), req.getStartDate(), req.getEndDate());
        } else {
            awsData = awsCostService.getCostsForMonth(req.getCompanyId(), req.getReportMonth());
        }
        
        String totalCost = (String) awsData.get("totalCost");
        String breakdown = (String) awsData.get("breakdown");
        String notes = (String) awsData.get("notes");
        
        CostReport entity = new CostReport();
        entity.setCompanyId(req.getCompanyId());
        entity.setReportMonth(req.getReportMonth());
        entity.setTotalCostEncrypted(encryptionService.encrypt(totalCost));
        entity.setBreakdownEncrypted(encryptionService.encrypt(breakdown));
        entity.setNotesEncrypted(encryptionService.encrypt(notes != null ? notes : ""));

        CostReport saved = repository.save(entity);
        return toResponse(saved, true);
    }

    public ReportResponse getReport(Long id, boolean decrypt) {
        CostReport entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reporte no encontrado: " + id));
        return toResponse(entity, decrypt);
    }

    public ReportResponse getRawDbRow(Long id) {
        CostReport entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reporte no encontrado: " + id));
        return toResponse(entity, false);
    }

    public List<ReportResponse> getReportsByCompany(String companyId, boolean canDecrypt) {
        return repository.findByCompanyId(companyId)
            .stream()
            .map(e -> toResponse(e, canDecrypt))
            .collect(Collectors.toList());
    }

    private ReportResponse toResponse(CostReport e, boolean decrypt) {
        ReportResponse r = new ReportResponse();
        r.setId(e.getId());
        r.setCompanyId(e.getCompanyId());
        r.setReportMonth(e.getReportMonth());
        r.setCreatedAt(e.getCreatedAt());
        r.setTotalCostEncrypted(e.getTotalCostEncrypted());
        r.setBreakdownEncrypted(e.getBreakdownEncrypted());
        r.setNotesEncrypted(e.getNotesEncrypted());

        if (decrypt) {
            r.setTotalCost(encryptionService.decrypt(e.getTotalCostEncrypted()));
            r.setBreakdown(encryptionService.decrypt(e.getBreakdownEncrypted()));
            r.setNotes(encryptionService.decrypt(e.getNotesEncrypted()));
        } else {
            r.setTotalCost("🔒 [ENCRIPTADO - No autorizado]");
            r.setBreakdown("🔒 [ENCRIPTADO - No autorizado]");
            r.setNotes("🔒 [ENCRIPTADO - No autorizado]");
        }

        return r;
    }
}

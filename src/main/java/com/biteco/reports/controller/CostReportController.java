package com.biteco.reports.controller;

import com.biteco.reports.dto.CreateReportRequest;
import com.biteco.reports.dto.ReportResponse;
import com.biteco.reports.service.CostReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class CostReportController {

    private final CostReportService service;

    public CostReportController(CostReportService service) {
        this.service = service;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "✅ UP",
            "service", "CostReportService",
            "message", "Servicio funcionando correctamente"
        ));
    }

    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody CreateReportRequest req,
                                          Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "❌ No autenticado. Use /api/auth/login primero"));
        }
        
        String companyId = (String) auth.getCredentials();
        req.setCompanyId(companyId);
        
        ReportResponse report = service.createReport(req);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "✅ Reporte creado exitosamente");
        response.put("reporte", report);
        response.put("visualizacion", report.toFormattedString());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReport(@PathVariable Long id, Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "❌ No autenticado. Use /api/auth/login primero"));
        }
        
        String userCompanyId = (String) auth.getCredentials();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        ReportResponse report = service.getReport(id, true);
        
        // Verificar permisos
        if (!isAdmin && !report.getCompanyId().equals(userCompanyId)) {
            report = service.getReport(id, false);
            Map<String, Object> response = new HashMap<>();
            response.put("warning", "⚠️ No tiene permiso para ver datos de esta empresa - Mostrando datos encriptados");
            response.put("reporte", report);
            response.put("visualizacion", report.toSecureString());
            return ResponseEntity.ok(response);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "✅ Acceso autorizado");
        response.put("reporte", report);
        response.put("visualizacion", report.toFormattedString());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/raw")
    public ResponseEntity<?> getRawRow(@PathVariable Long id, Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "❌ No autenticado. Use /api/auth/login primero"));
        }
        
        ReportResponse report = service.getRawDbRow(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "🔒 Datos en su estado original (encriptados en DB)");
        response.put("reporte", report);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> getByCompany(@PathVariable String companyId,
                                          Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "❌ No autenticado. Use /api/auth/login primero"));
        }
        
        String userCompanyId = (String) auth.getCredentials();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        boolean canDecrypt = isAdmin || companyId.equals(userCompanyId);
        java.util.List<ReportResponse> reports = service.getReportsByCompany(companyId, canDecrypt);
        
        Map<String, Object> response = new HashMap<>();
        response.put("cantidad", reports.size());
        response.put("reportes", reports);
        
        if (!canDecrypt) {
            response.put("warning", "⚠️ No tiene permiso para ver datos desencriptados de esta empresa");
            response.put("mensaje", "Los datos mostrados están encriptados");
        } else {
            response.put("message", "✅ Acceso autorizado");
        }
        
        return ResponseEntity.ok(response);
    }
}

package com.biteco.reports.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class SourceDataService {
    
    public Map<String, Object> executeQuery(String queryId, String parameters, 
                                            String companyId, String reportMonth) {
        Map<String, Object> reportData = new HashMap<>();
        
        if ("monthly_costs".equals(queryId)) {
            reportData.put("totalCost", "15420.50");
            reportData.put("breakdown", "[{category='Marketing', total=8500.00}, {category='Operations', total=6920.50}]");
            reportData.put("notes", "Reporte generado para " + reportMonth);
        } else {
            reportData.put("totalCost", "0.00");
            reportData.put("breakdown", "{}");
            reportData.put("notes", "Datos de prueba");
        }
        
        return reportData;
    }
}

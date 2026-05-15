package com.biteco.reports.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportResponse {
    private Long   id;
    private String companyId;
    private String reportMonth;
    private String totalCost;
    private String breakdown;
    private String notes;
    private String totalCostEncrypted;
    private String breakdownEncrypted;
    private String notesEncrypted;
    private LocalDateTime createdAt;

    public Long   getId() { return id; }
    public String getCompanyId() { return companyId; }
    public String getReportMonth() { return reportMonth; }
    public String getTotalCost() { return totalCost; }
    public String getBreakdown() { return breakdown; }
    public String getNotes() { return notes; }
    public String getTotalCostEncrypted() { return totalCostEncrypted; }
    public String getBreakdownEncrypted() { return breakdownEncrypted; }
    public String getNotesEncrypted() { return notesEncrypted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : null;
    }

    public void setId(Long v) { this.id = v; }
    public void setCompanyId(String v) { this.companyId = v; }
    public void setReportMonth(String v) { this.reportMonth = v; }
    public void setTotalCost(String v) { this.totalCost = v; }
    public void setBreakdown(String v) { this.breakdown = v; }
    public void setNotes(String v) { this.notes = v; }
    public void setTotalCostEncrypted(String v) { this.totalCostEncrypted = v; }
    public void setBreakdownEncrypted(String v) { this.breakdownEncrypted = v; }
    public void setNotesEncrypted(String v) { this.notesEncrypted = v; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    
    public String toFormattedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("═".repeat(60)).append("\n");
        sb.append("   📊 REPORTE DE COSTOS - BITE.CO\n");
        sb.append("═".repeat(60)).append("\n");
        sb.append(String.format("   ID: %d\n", id));
        sb.append(String.format("   Empresa: %s\n", companyId));
        sb.append(String.format("   Período: %s\n", reportMonth));
        sb.append(String.format("   Fecha creación: %s\n", getFormattedCreatedAt()));
        sb.append("─".repeat(60)).append("\n");
        sb.append("   📋 DESGLOSE DE GASTOS:\n");
        sb.append("─".repeat(60)).append("\n");
        
        if (breakdown != null && !breakdown.startsWith("🔒")) {
            String[] items = breakdown.split(",");
            for (String item : items) {
                String[] parts = item.split(":");
                if (parts.length == 2) {
                    sb.append(String.format("   • %-20s %s\n", parts[0].trim(), parts[1].trim()));
                } else {
                    sb.append(String.format("   • %s\n", item.trim()));
                }
            }
        } else {
            sb.append("   🔒 Datos encriptados - No autorizado\n");
        }
        
        sb.append("─".repeat(60)).append("\n");
        sb.append(String.format("   TOTAL GENERAL: %s\n", totalCost));
        sb.append("─".repeat(60)).append("\n");
        
        if (notes != null && !notes.startsWith("🔒")) {
            sb.append(String.format("   📝 NOTAS: %s\n", notes));
            sb.append("─".repeat(60)).append("\n");
        }
        
        sb.append("═".repeat(60)).append("\n");
        sb.append("   🔒 Datos almacenados encriptados en DB\n");
        sb.append("═".repeat(60)).append("\n");
        
        return sb.toString();
    }
    
    public String toSecureString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("═".repeat(60)).append("\n");
        sb.append("   🔒 ACCESO RESTRINGIDO - DATOS PROTEGIDOS\n");
        sb.append("═".repeat(60)).append("\n");
        sb.append(String.format("   ID: %d\n", id));
        sb.append(String.format("   Empresa: %s\n", companyId));
        sb.append("─".repeat(60)).append("\n");
        sb.append("   ⚠️  No tiene permiso para ver esta información\n");
        sb.append("   Los datos están protegidos mediante encriptación\n");
        sb.append("─".repeat(60)).append("\n");
        sb.append(String.format("   Total: %s\n", totalCost));
        sb.append("═".repeat(60)).append("\n");
        return sb.toString();
    }
}

package com.biteco.reports.dto;

public class CreateReportRequest {
    private String companyId;
    private String reportMonth;  
    private String startDate;    
    private String endDate;      

    public CreateReportRequest() {
        java.time.LocalDate now = java.time.LocalDate.now();
        this.reportMonth = now.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String v) { this.companyId = v; }

    public String getReportMonth() { return reportMonth; }
    public void setReportMonth(String v) { this.reportMonth = v; }
    
    public String getStartDate() { return startDate; }
    public void setStartDate(String v) { this.startDate = v; }
    
    public String getEndDate() { return endDate; }
    public void setEndDate(String v) { this.endDate = v; }
}

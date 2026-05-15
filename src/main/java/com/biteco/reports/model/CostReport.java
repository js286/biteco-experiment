package com.biteco.reports.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "cost_reports")
public class CostReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private String companyId;

    @Column(name = "report_month", nullable = false)
    private String reportMonth;

    //AES-256-GCM ciphertext + IV (Base64)
    @Column(name = "total_cost_encrypted", nullable = false, columnDefinition = "TEXT")
    private String totalCostEncrypted;

    @Column(name = "breakdown_encrypted", nullable = false, columnDefinition = "TEXT")
    private String breakdownEncrypted;

    @Column(name = "notes_encrypted", columnDefinition = "TEXT")
    private String notesEncrypted;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getReportMonth() { return reportMonth; }
    public void setReportMonth(String reportMonth) { this.reportMonth = reportMonth; }

    public String getTotalCostEncrypted() { return totalCostEncrypted; }
    public void setTotalCostEncrypted(String totalCostEncrypted) { this.totalCostEncrypted = totalCostEncrypted; }

    public String getBreakdownEncrypted() { return breakdownEncrypted; }
    public void setBreakdownEncrypted(String breakdownEncrypted) { this.breakdownEncrypted = breakdownEncrypted; }

    public String getNotesEncrypted() { return notesEncrypted; }
    public void setNotesEncrypted(String notesEncrypted) { this.notesEncrypted = notesEncrypted; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}

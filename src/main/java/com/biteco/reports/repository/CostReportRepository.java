package com.biteco.reports.repository;

import com.biteco.reports.model.CostReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CostReportRepository extends JpaRepository<CostReport, Long> {
    List<CostReport> findByCompanyId(String companyId);
}

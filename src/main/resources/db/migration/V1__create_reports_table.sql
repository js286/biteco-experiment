CREATE TABLE IF NOT EXISTS cost_reports (
    id BIGSERIAL PRIMARY KEY,
    company_id VARCHAR(255) NOT NULL,
    report_month VARCHAR(7) NOT NULL,
    total_cost_encrypted TEXT NOT NULL,
    breakdown_encrypted TEXT NOT NULL,
    notes_encrypted TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_company_month ON cost_reports(company_id, report_month);
CREATE INDEX IF NOT EXISTS idx_created_at ON cost_reports(created_at);

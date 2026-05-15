CREATE TABLE IF NOT EXISTS report_access_audit (
    id BIGSERIAL PRIMARY KEY,
    report_id BIGINT NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    user_role VARCHAR(50) NOT NULL,
    access_type VARCHAR(50) NOT NULL,
    access_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent TEXT
);

CREATE INDEX IF NOT EXISTS idx_audit_report ON report_access_audit(report_id);
CREATE INDEX IF NOT EXISTS idx_audit_user ON report_access_audit(user_id);
CREATE INDEX IF NOT EXISTS idx_audit_timestamp ON report_access_audit(access_timestamp);

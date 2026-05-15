CREATE TABLE IF NOT EXISTS query_definitions (
    id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    sql_query TEXT NOT NULL,
    parameters JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO query_definitions (id, name, sql_query) VALUES
('monthly_costs', 'Costos Mensuales', 
 'SELECT SUM(amount) as total, category FROM source_transactions WHERE company_id = :companyId AND transaction_date BETWEEN :startDate AND :endDate GROUP BY category'),
('operational_metrics', 'Métricas Operacionales',
 'SELECT * FROM operational_metrics WHERE company_id = :companyId AND metric_month = :reportMonth'),
('department_costs', 'Costos por Departamento',
 'SELECT department, SUM(amount) as total FROM department_expenses WHERE company_id = :companyId AND expense_date BETWEEN :startDate AND :endDate GROUP BY department')
ON CONFLICT (id) DO NOTHING;

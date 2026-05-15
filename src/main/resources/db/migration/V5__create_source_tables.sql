-- Tabla para transacciones fuente (datos reales)
CREATE TABLE IF NOT EXISTS source_transactions (
    id BIGSERIAL PRIMARY KEY,
    company_id VARCHAR(255) NOT NULL,
    transaction_date DATE NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    category VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar datos de ejemplo para retail-colombia-001
INSERT INTO source_transactions (company_id, transaction_date, amount, category, description) VALUES
('retail-colombia-001', '2026-05-01', 8500.00, 'Marketing', 'Campaña publicitaria Mayo'),
('retail-colombia-001', '2026-05-15', 6920.50, 'Operaciones', 'Mantenimiento sistemas'),
('retail-colombia-001', '2026-05-20', 2100.00, 'Infraestructura', 'Servidores AWS'),
('fintech-bogota-002', '2026-05-05', 8900.00, 'Desarrollo', 'Plataforma microservicios'),
('fintech-bogota-002', '2026-05-10', 5600.00, 'Bases de Datos', 'RDS Aurora'),
('fintech-bogota-002', '2026-05-20', 3400.00, 'Caché', 'Redis clusters'),
('saas-b2b-medellin-003', '2026-05-02', 15400.00, 'Orquestación', 'EKS clusters'),
('saas-b2b-medellin-003', '2026-05-12', 8900.00, 'Almacenamiento', 'S3 + Glacier'),
('saas-b2b-medellin-003', '2026-05-22', 4200.00, 'CDN', 'CloudFront'),
('iot-manufactura-004', '2026-05-03', 8500.00, 'IoT Core', 'Dispositivos conectados'),
('iot-manufactura-004', '2026-05-17', 3400.00, 'TimeStream', 'Almacenamiento telemetría'),
('media-streaming-005', '2026-05-04', 28000.00, 'Distribución', 'CloudFront'),
('media-streaming-005', '2026-05-18', 12000.00, 'Transcodificación', 'MediaConvert');

-- Tabla para métricas operacionales
CREATE TABLE IF NOT EXISTS operational_metrics (
    id BIGSERIAL PRIMARY KEY,
    company_id VARCHAR(255) NOT NULL,
    metric_month VARCHAR(7) NOT NULL,
    metric_name VARCHAR(100) NOT NULL,
    metric_value DECIMAL(15,2) NOT NULL,
    unit VARCHAR(50)
);

-- Tabla para gastos por departamento
CREATE TABLE IF NOT EXISTS department_expenses (
    id BIGSERIAL PRIMARY KEY,
    company_id VARCHAR(255) NOT NULL,
    expense_date DATE NOT NULL,
    department VARCHAR(100) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    description TEXT
);

CREATE TABLE farmers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    mobile VARCHAR(15) UNIQUE NOT NULL,
    village VARCHAR(100),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_farmers_customer_id ON farmers(customer_id);
CREATE INDEX idx_farmers_mobile ON farmers(mobile);
CREATE INDEX idx_farmers_status ON farmers(status);

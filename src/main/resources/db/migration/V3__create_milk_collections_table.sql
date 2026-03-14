CREATE TABLE milk_collections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    farmer_id UUID NOT NULL REFERENCES farmers(id),
    date DATE NOT NULL,
    milk_liter DECIMAL(10,2) NOT NULL,
    fat DECIMAL(5,2),
    snf DECIMAL(5,2),
    degree DECIMAL(5,2) NOT NULL,
    rate DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_milk_collections_farmer_id ON milk_collections(farmer_id);
CREATE INDEX idx_milk_collections_date ON milk_collections(date);
CREATE INDEX idx_milk_collections_farmer_date ON milk_collections(farmer_id, date);

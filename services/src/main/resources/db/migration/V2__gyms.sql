CREATE TABLE gyms (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(50) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL DEFAULT 'Portugal',
    phone VARCHAR(50),
    email VARCHAR(255),
    timezone VARCHAR(50) NOT NULL DEFAULT 'Europe/Lisbon',
    opening_time TIME,
    closing_time TIME,
    is_active BOOLEAN NOT NULL DEFAULT true,
    max_capacity INTEGER,
    owner_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_gyms_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE INDEX idx_gyms_owner_id ON gyms(owner_id);
CREATE INDEX idx_gyms_is_active ON gyms(is_active);
CREATE INDEX idx_gyms_city ON gyms(city);

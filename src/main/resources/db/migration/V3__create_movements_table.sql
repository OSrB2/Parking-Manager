CREATE TABLE tb_movements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vechicle_id UUID NOT NULL,
    enterprise_id UUID NOT NULL,
    entry_time TIMESTAMP NOT NULL,
    departure_time TIMESTAMP,
    status VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_vehicle FOREIGN KEY (vechicle_id) REFERENCES tb_vehicles(id),
    CONSTRAINT fk_enterprise FOREIGN KEY (enterprise_id) REFERENCES tb_enterprises(id)
);
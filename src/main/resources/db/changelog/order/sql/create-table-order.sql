CREATE TABLE IF NOT EXISTS "order" (
                                       id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    customer_id BIGINT references customer (id),
    status VARCHAR(10) NOT NULL,
    delivery_address VARCHAR(255) NOT NULL
    );

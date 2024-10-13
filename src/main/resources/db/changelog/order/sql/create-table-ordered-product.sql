CREATE TABLE IF NOT EXISTS ordered_product
(   id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    order_id  UUID  NOT NULL REFERENCES "order" (id),
    product_id UUID  NOT NULL REFERENCES product (id),
    qty    NUMERIC(10, 2) NOT NULL,
    price   NUMERIC(10, 2) NOT NULL
    );
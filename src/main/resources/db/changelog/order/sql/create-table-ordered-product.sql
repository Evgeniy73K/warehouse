CREATE TABLE IF NOT EXISTS ordered_product
(
    order_id UUID NOT NULL REFERENCES "order" (id),
    product_id UUID NOT NULL REFERENCES product (id),
    qty  NUMERIC(10, 2) NOT NULL,
    price  NUMERIC(10, 2) NOT NULL,
    PRIMARY KEY (order_id, product_id)
    );
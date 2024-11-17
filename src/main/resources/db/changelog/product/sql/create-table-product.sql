CREATE TABLE IF NOT EXISTS product
(
    id               UUID PRIMARY KEY         NOT NULL DEFAULT gen_random_uuid(),
    name             VARCHAR,
    article          UUID UNIQUE              NOT NULL,
    dictionary       VARCHAR,
    category         VARCHAR,
    price            numeric(10, 2),
    qty              numeric(10, 2),
    inserted_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    last_qty_changed TIMESTAMP WITH TIME ZONE
);
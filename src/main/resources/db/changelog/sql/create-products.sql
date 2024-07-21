CREATE SCHEMA IF NOT EXISTS warehouse;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE OR REPLACE FUNCTION gen_random_uuid()
RETURNS UUID AS $$
BEGIN
RETURN uuid_generate_v4();
END;
$$
LANGUAGE plpgsql;

CREATE TYPE product_category AS ENUM ('FRUITS', 'VEGETABLES');

CREATE TABLE IF NOT EXISTS warehouse.products
(
    id               UUID PRIMARY KEY         NOT NULL DEFAULT gen_random_uuid(),
    name              VARCHAR,
    article           UUID UNIQUE NOT NULL,
    dictionary        VARCHAR,
    category          product_category,
    price             DECIMAL(10, 2),
    qty               DECIMAL(10, 2),
    inserted_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    last_qty_changed        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
    );

COMMENT ON COLUMN warehouse.products.id
IS 'Уникальный идентификатор записи';

COMMENT ON COLUMN warehouse.products.name
IS 'Название товара';

COMMENT ON COLUMN warehouse.products.article
IS 'Артикул товара';

COMMENT ON COLUMN warehouse.products.dictionary
IS 'Описание товара';

COMMENT ON COLUMN warehouse.products.category
IS 'Категория товара';

COMMENT ON COLUMN warehouse.products.price
IS 'Цена товара';

COMMENT ON COLUMN warehouse.products.qty
IS 'Количество товара';

COMMENT ON COLUMN warehouse.products.inserted_at
IS 'Дата и время создания записи';

COMMENT ON COLUMN warehouse.products.last_qty_changed
IS 'Дата и время последнего обновления записи';
ALTER TABLE IF EXISTS product
    ADD COLUMN IF NOT EXISTS
        is_available boolean NULL DEFAULT false;

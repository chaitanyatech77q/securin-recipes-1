CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE IF NOT EXISTS recipes (
  id           BIGSERIAL PRIMARY KEY,
  cuisine      VARCHAR(255),
  title        VARCHAR(512) NOT NULL,
  rating       REAL,
  prep_time    INT,
  cook_time    INT,
  total_time   INT,
  description  TEXT,
  nutrients    JSONB,
  serves       VARCHAR(64),
  calories_int INT GENERATED ALWAYS AS (
    NULLIF(regexp_replace(nutrients->>'calories', '[^0-9]', '', 'g'), '')::INT
  ) STORED
);

CREATE INDEX IF NOT EXISTS idx_recipes_rating_desc  ON recipes (rating DESC NULLS LAST);
CREATE INDEX IF NOT EXISTS idx_recipes_title_trgm   ON recipes USING GIN (lower(title) gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_recipes_cuisine      ON recipes (cuisine);
CREATE INDEX IF NOT EXISTS idx_recipes_nutrients    ON recipes USING GIN (nutrients jsonb_path_ops);

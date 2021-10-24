CREATE TABLE reversal (
id uuid NOT NULL PRIMARY KEY,
original TEXT NOT NULL,
reversed TEXT NOT NULL,
created TIMESTAMP DEFAULT NOW()
);

CREATE INDEX ON reversal (created);
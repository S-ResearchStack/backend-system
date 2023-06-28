create TABLE "respiratory_rates"
(
    "id"      SERIAL       NOT NULL,
    "user_id" VARCHAR(320) NOT NULL,
    "time"    timestamptz  NOT NULL,
    "rpm"     FLOAT8       NOT NULL,
    PRIMARY KEY ("id")
);

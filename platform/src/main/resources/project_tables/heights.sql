create TABLE "heights"
(
    "id"      SERIAL       NOT NULL,
    "user_id" VARCHAR(320) NOT NULL,
    "time"    timestamptz  NOT NULL,
    "meters"  FLOAT8       NOT NULL,
    PRIMARY KEY ("id")
);

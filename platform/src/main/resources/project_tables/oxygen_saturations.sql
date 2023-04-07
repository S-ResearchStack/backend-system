create TABLE "oxygen_saturations"
(
    "id"      SERIAL       NOT NULL,
    "user_id" VARCHAR(320) NOT NULL,
    "time"    timestamptz  NOT NULL,
    "value"   FLOAT4       NOT NULL,
    PRIMARY KEY ("id")
);

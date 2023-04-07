create TABLE "weights"
(
    "id"        SERIAL       NOT NULL,
    "user_id"   VARCHAR(320) NOT NULL,
    "time"      timestamptz  NOT NULL,
    "kilograms" FLOAT4       NOT NULL,
    PRIMARY KEY ("id")
);

create TABLE "blood_glucoses"
(
    "id"                   SERIAL       NOT NULL,
    "user_id"              VARCHAR(320) NOT NULL,
    "time"                 timestamptz  NOT NULL,
    "millimoles_per_liter" FLOAT8       NOT NULL,
    "specimen_source"      VARCHAR(32),
    "meal_type"            VARCHAR(32),
    "relation_to_meal"     VARCHAR(32),
    PRIMARY KEY ("id")
);

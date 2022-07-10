create TABLE "blood_pressures"
(
    "id"                   SERIAL       NOT NULL,
    "user_id"              VARCHAR(320) NOT NULL,
    "time"                 timestamptz  NOT NULL,
    "systolic"             FLOAT8       NOT NULL,
    "diastolic"            FLOAT8       NOT NULL,
    "body_position"        VARCHAR(32),
    "measurement_location" VARCHAR(32),
    PRIMARY KEY ("id")
);

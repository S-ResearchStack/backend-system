create TABLE "ecg"
(
    "id" SERIAL NOT NULL,
    "user_id" VARCHAR(320) NOT NULL,
    "time" timestamptz NOT NULL,
    "min_threshold_mv" FLOAT8 NOT NULL,
    "max_threshold_mv" FLOAT8 NOT NULL,
    "ppg1" INT8 NOT NULL,
    "ppg2" INT8,
    "ecg1_mv" FLOAT8 NOT NULL,
    "ecg2_mv" FLOAT8 NOT NULL,
    "ecg3_mv" FLOAT8 NOT NULL,
    "ecg4_mv" FLOAT8 NOT NULL,
    "ecg5_mv" FLOAT8 NOT NULL,
    "ecg6_mv" FLOAT8,
    "ecg7_mv" FLOAT8,
    "ecg8_mv" FLOAT8,
    "ecg9_mv" FLOAT8,
    "ecg10_mv" FLOAT8,
    PRIMARY KEY ("id")
);

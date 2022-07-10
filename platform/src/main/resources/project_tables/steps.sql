create TABLE "steps"
(
    "id"         SERIAL       NOT NULL,
    "user_id"    VARCHAR(320) NOT NULL,
    "start_time" timestamptz  NOT NULL,
    "end_time"   timestamptz  NOT NULL,
    "count"      INT8         NOT NULL,
    PRIMARY KEY ("id")
);

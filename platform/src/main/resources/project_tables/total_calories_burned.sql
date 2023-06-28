create TABLE "total_calories_burned"
(
    "id"         SERIAL       NOT NULL,
    "user_id"    VARCHAR(320) NOT NULL,
    "start_time" timestamptz  NOT NULL,
    "end_time"   timestamptz  NOT NULL,
    "calories"   FLOAT8       NOT NULL,
    PRIMARY KEY ("id")
);

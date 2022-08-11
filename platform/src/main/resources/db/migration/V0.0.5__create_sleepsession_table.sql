create TABLE "sleepsessions"
(
    "id"         SERIAL       NOT NULL,
    "user_id"    VARCHAR(320) NOT NULL,
    "start_time" timestamptz  NOT NULL,
    "end_time"   timestamptz  NOT NULL,
    "title"      TEXT,
    "notes"      TEXT,
    PRIMARY KEY ("id")
);

CREATE INDEX sleepsessions_user_id_index ON sleepsessions (user_id);

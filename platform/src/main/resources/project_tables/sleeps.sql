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

create TABLE "sleepstage"
(
    "id"         SERIAL       NOT NULL,
    "user_id"    VARCHAR(320) NOT NULL,
    "start_time" timestamptz  NOT NULL,
    "end_time"   timestamptz  NOT NULL,
    "stage"      VARCHAR(32)  NOT NULL,
    PRIMARY KEY ("id")
);

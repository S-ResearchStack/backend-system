create TABLE "steps"
(
    "id"         SERIAL       NOT NULL,
    "user_id"    VARCHAR(320) NOT NULL,
    "start_time" timestamptz  NOT NULL,
    "end_time"   timestamptz  NOT NULL,
    "count"      INT8         NOT NULL,
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

CREATE INDEX steps_user_id_index ON steps (user_id);
CREATE INDEX sleepstage_user_id_index ON sleepstage (user_id);

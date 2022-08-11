create TABLE "heartrates"
(
    "id"         SERIAL      NOT NULL,
    "user_id"    VARCHAR(320)      NOT NULL,
    "time"       timestamptz NOT NULL,
    "bpm"        INT8     NOT NULL,
    PRIMARY KEY ("id")
);

CREATE INDEX heartrates_user_id_index ON heartrates (user_id);

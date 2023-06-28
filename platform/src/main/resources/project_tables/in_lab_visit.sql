create TABLE "in_lab_visit"
(
    "id"             SERIAL       NOT NULL,
    "user_id"        VARCHAR(320) NOT NULL,
    "checked_in_by"  VARCHAR(320) NOT NULL,
    "start_time"     timestamptz  NOT NULL,
    "end_time"       timestamptz  NOT NULL,
    "notes"          TEXT,
    PRIMARY KEY ("id")
);

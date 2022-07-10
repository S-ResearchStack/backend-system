create TABLE "task_results"
(
    "id"            SERIAL          NOT NULL,
    "revision_id"   INT             NOT NULL,
    "task_id"       VARCHAR(320)    NOT NULL,
    "user_id"       VARCHAR(320)    NOT NULL,
    "started_at"    timestamp       NOT NULL,
    "submitted_at"  timestamp       NOT NULL,
    PRIMARY KEY ("id")
);

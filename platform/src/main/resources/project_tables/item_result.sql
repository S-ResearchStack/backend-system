create TABLE "item_results"
(
    "id"            SERIAL          NOT NULL,
    "revision_id"   INT             NOT NULL,
    "task_id"       VARCHAR(320)    NOT NULL,
    "user_id"       VARCHAR(320)    NOT NULL,
    "item_name"     VARCHAR(320)    NOT NULL,
    "result"        VARCHAR(320)    NOT NULL,
    PRIMARY KEY ("id")
);

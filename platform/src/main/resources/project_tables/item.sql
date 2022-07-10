create TABLE "items"
(
    "id"            SERIAL          NOT NULL,
    "revision_id"   INT             NOT NULL,
    "task_id"       VARCHAR(320)    NOT NULL,
    "name"          VARCHAR(320)    NOT NULL,
    "contents"      JSONB           NOT NULL,
    "type"          VARCHAR(20)     NOT NULL,
    "sequence"         INT             NOT NULL,
    PRIMARY KEY ("id")
);

CREATE INDEX items_revision_id_index ON items (revision_id);
CREATE INDEX items_task_id_index ON items (task_id);

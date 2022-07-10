create TABLE "tasks"
(
    "revision_id"  SERIAL       NOT NULL,
    "id"           VARCHAR(320) NOT NULL,
    "properties"   JSONB        NOT NULL,
    "status"       VARCHAR(20)  NOT NULL,
    "created_at"   timestamptz  NOT NULL,
    "published_at" timestamptz,
    "outdated_at"  timestamptz,
    "deleted_at"   timestamptz,
    PRIMARY KEY ("revision_id")
);

CREATE INDEX tasks_id_index ON tasks (id);

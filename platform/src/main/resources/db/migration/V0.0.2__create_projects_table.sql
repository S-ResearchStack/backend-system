create TABLE "projects"
(
    "id"         SERIAL      NOT NULL,
    "name"       varchar(32) NOT NULL,
    "info"       JSONB       NOT NULL,
    "created_at" timestamptz NOT NULL,
    "deleted_at" timestamptz,
    PRIMARY KEY ("id")
);

CREATE UNIQUE INDEX name_index ON projects (name);

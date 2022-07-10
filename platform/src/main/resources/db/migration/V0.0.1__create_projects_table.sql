create TABLE "projects"
(
    "id"         SERIAL      NOT NULL,
    "name"       varchar(32) NOT NULL,
    "info"       JSONB       NOT NULL,
    "is_open"    BOOLEAN     NOT NULL DEFAULT TRUE,
    "created_at" timestamptz NOT NULL,
    "deleted_at" timestamptz,
    PRIMARY KEY ("id")
);

CREATE UNIQUE INDEX projects_name_idx ON projects (name);

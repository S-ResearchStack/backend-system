create TABLE "users"
(
    "id"         VARCHAR(320)         NOT NULL,
    "sub"        VARCHAR(255)        NOT NULL,
    "provider"   VARCHAR(32)         NOT NULL,
    "created_at" timestamptz         NOT NULL,
    "deleted_at" timestamptz,
    PRIMARY KEY ("id")
);

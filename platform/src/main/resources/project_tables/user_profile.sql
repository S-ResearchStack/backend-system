create TABLE "user_profiles"
(
    "user_id"         VARCHAR(320) NOT NULL,
    "profile"         JSONB        NOT NULL,
    "last_synced_at"  timestamp    NOT NULL,
    PRIMARY KEY ("user_id")
);

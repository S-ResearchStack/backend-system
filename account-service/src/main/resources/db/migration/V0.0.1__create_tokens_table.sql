create TABLE "tokens"
(
    "id"             SERIAL        NOT NULL,
    "account_id"     VARCHAR(36)   NOT NULL,
    "access_token"   TEXT          NOT NULL,
    "refresh_token"  VARCHAR(500)  NOT NULL,
    "expired_at"     INT8          NOT NULL,
    PRIMARY KEY ("id")
);

CREATE UNIQUE INDEX access_token_and_refresh_token_idx ON tokens (access_token, refresh_token);

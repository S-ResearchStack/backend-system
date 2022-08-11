create TABLE "heartrates"
(
    "id" SERIAL NOT NULL,
    "user_id" VARCHAR(320) NOT NULL,
    "time" timestamptz NOT NULL,
    "bpm" INT8 NOT NULL,
    PRIMARY KEY ("id")
);

create TABLE "study_joins"
(
    "id"         SERIAL       NOT NULL,
    "subject_id"     VARCHAR(320) NOT NULL,
    "study_id"   VARCHAR(320) NOT NULL,
    "subject_number" BIGINT       NOT NULL,
    PRIMARY KEY ("id")
);

CREATE UNIQUE INDEX study_subjects_idx ON study_joins (subject_id, study_id);

DROP TABLE IF EXISTS endpoint_hit;

CREATE TABLE IF NOT EXISTS endpoint_hit
(
    id
    BIGINT
    GENERATED
    ALWAYS AS
    IDENTITY
    PRIMARY
    KEY,
    app
    VARCHAR
(
    64
) NOT NULL,
    uri VARCHAR NOT NULL,
    ip VARCHAR
(
    16
) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
    );

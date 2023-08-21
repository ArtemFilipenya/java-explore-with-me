DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS compilation_events;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS compilation;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    VARCHAR
(
    250
) NOT NULL,
    email VARCHAR
(
    254
) NOT NULL CONSTRAINT UQ_USER_EMAIL UNIQUE
    );

CREATE TABLE IF NOT EXISTS categories
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    VARCHAR
(
    50
) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS events
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    title
    VARCHAR
(
    120
),
    description VARCHAR
(
    7000
),
    annotation VARCHAR
(
    2000
),
    category_id BIGINT,
    initiator_id BIGINT,
    location_id BIGINT,
    paid BOOLEAN DEFAULT false,
    participant_limit INT DEFAULT 0,
    confirmed_requests INT,
    request_moderation BOOLEAN DEFAULT true,
    state VARCHAR,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_date TIMESTAMP
                         WITHOUT TIME ZONE NOT NULL,
    published_on TIMESTAMP
                         WITHOUT TIME ZONE
    );

CREATE TABLE IF NOT EXISTS compilation
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    title
    VARCHAR
    NOT
    NULL
    CONSTRAINT
    UQ_COMPILATION_TITLE
    UNIQUE,
    pinned
    BOOLEAN
    DEFAULT
    false
);

CREATE TABLE IF NOT EXISTS locations
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    lat
    FLOAT
    NOT
    NULL,
    lon
    FLOAT
    NOT
    NULL
);

CREATE TABLE compilation_events
(
    compilation_id BIGINT,
    event_id       BIGINT,
    PRIMARY KEY (compilation_id, event_id)
);


CREATE TABLE IF NOT EXISTS requests
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    status
    VARCHAR
(
    255
) NOT NULL,
    event_id BIGINT REFERENCES events
(
    id
) ON DELETE CASCADE,
    requester_id BIGINT REFERENCES users
(
    id
)
  ON DELETE CASCADE,
    created TIMESTAMP
  WITHOUT TIME ZONE NOT NULL
    );

ALTER TABLE events
    ADD FOREIGN KEY (category_id) REFERENCES categories (id);
ALTER TABLE events
    ADD FOREIGN KEY (initiator_id) REFERENCES users (id);
ALTER TABLE events
    ADD FOREIGN KEY (location_id) REFERENCES locations (id);

ALTER TABLE compilation_events
    ADD FOREIGN KEY (compilation_id) REFERENCES compilation (id);
ALTER TABLE compilation_events
    ADD FOREIGN KEY (event_id) REFERENCES events (id);

ALTER TABLE requests
    ADD FOREIGN KEY (event_id) REFERENCES events (id);
ALTER TABLE requests
    ADD FOREIGN KEY (requester_id) REFERENCES users (id);

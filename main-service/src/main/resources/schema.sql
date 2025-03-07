CREATE TABLE IF NOT EXISTS users
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email     VARCHAR(255) NOT NULL,
    name      VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT users_unique UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id        INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name      VARCHAR(255) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT categories_unique UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat REAL NOT NULL,
    lon REAL NOT NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id)

);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR(120) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    category_id INT NOT NULL,
    initiator_id BIGINT NOT NULL,
    description VARCHAR(7000) NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    location_id BIGINT NOT NULL,
    paid BOOLEAN NOT NULL DEFAULT false,
    participant_limit BIGINT NOT NULL DEFAULT 0,
    request_moderation BOOLEAN NOT NULL DEFAULT true,
    state VARCHAR NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT pk_events PRIMARY KEY (id),

    CONSTRAINT events_category_id_fk
        FOREIGN KEY(category_id)
            REFERENCES categories(id)
                ON DELETE RESTRICT,

    CONSTRAINT events_initiator_id_fk
        FOREIGN KEY(initiator_id)
            REFERENCES users(id)
                ON DELETE CASCADE,

    CONSTRAINT events_location_id_fk
        FOREIGN KEY(location_id)
            REFERENCES locations(id)
               ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS participations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status VARCHAR NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT pk_participation PRIMARY KEY (id),
    CONSTRAINT participations_requester_fk
        FOREIGN KEY(requester_id)
            REFERENCES users(id)
                ON DELETE CASCADE,

    CONSTRAINT participations_event_fk
        FOREIGN KEY(event_id)
            REFERENCES events(id)
                ON DELETE CASCADE
);

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
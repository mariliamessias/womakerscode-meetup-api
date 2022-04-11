CREATE TABLE IF NOT EXISTS address (
    id bigint NOT NULL AUTO_INCREMENT,
    public_place VARCHAR(128) NOT NULL,
    zip_code VARCHAR(100) NOT NULL,
    neighborhood VARCHAR(150) NOT NULL,
    city VARCHAR(150) NOT NULL,
    country VARCHAR(100) NOT NULL,
    number INTEGER NOT NULL,
    created_at DATE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS person (
    id bigint NOT NULL AUTO_INCREMENT,
    address_id bigint NOT NULL,
    name VARCHAR(150) NOT NULL,
    birth_date DATE NOT NULL,
    email VARCHAR(150) NOT NULL,
    created_at DATE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (address_id) REFERENCES address(id)
);

CREATE TABLE IF NOT EXISTS users (
    id bigint NOT NULL AUTO_INCREMENT,
    person_id bigint NOT NULL,
    role VARCHAR(100) NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    password VARCHAR(150) NOT NULL,
    created_at DATE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (person_id) REFERENCES person(id)
);

CREATE TABLE IF NOT EXISTS event (
    id bigint NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    status VARCHAR(150) NOT NULL,
    maximun_spots INTEGER NOT NULL,
    alocated_spots INTEGER NOT NULL,
    created_at DATE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS registration (
    id bigint NOT NULL AUTO_INCREMENT,
    user_id bigint NOT NULL,
    event_id bigint NOT NULL,
    status VARCHAR(128) NOT NULL,
    description VARCHAR(100) NOT NULL,
    created_at DATE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (event_id) REFERENCES event(id)
);
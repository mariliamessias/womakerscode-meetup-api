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
    user_name VARCHAR(100) NOT NULL,
    name VARCHAR(150) NOT NULL,
    birth_date DATE NOT NULL,
    email VARCHAR(150) NOT NULL,
    created_at DATE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (address_id) REFERENCES address(id)
);

CREATE TABLE IF NOT EXISTS event (
    id bigint NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    status VARCHAR(150) NOT NULL,
    maximun_spots INTEGER NOT NULL,
    alocated_spots INTEGER NOT NULL,
    created_at DATE NOT NULL,
    event_date DATE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS registration (
    id bigint NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(100) NOT NULL,
    event_id bigint NOT NULL,
    status VARCHAR(128) NOT NULL,
    description VARCHAR(100) NOT NULL,
    created_at DATE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (event_id) REFERENCES event(id)
);
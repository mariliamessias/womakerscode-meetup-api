CREATE TABLE IF NOT EXISTS registration (
    id INTEGER NOT NULL AUTO_INCREMENT,
    status VARCHAR(128) NOT NULL,
    description VARCHAR(100) NOT NULL,
    created_at DATE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS address (
    id INTEGER NOT NULL AUTO_INCREMENT,
    public_place VARCHAR(128) NOT NULL,
    zip_code VARCHAR(100) NOT NULL,
    neighborhood VARCHAR(150) NOT NULL,
    city VARCHAR(150) NOT NULL,
    country VARCHAR(100) NOT NULL,
    number INTEGER NOT NULL,
    created_at DATE NOT NULL,
    PRIMARY KEY (id)
);
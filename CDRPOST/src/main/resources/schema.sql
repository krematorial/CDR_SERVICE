CREATE TABLE IF NOT EXISTS subscriber (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS cdrcall (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    call_type VARCHAR(2) NOT NULL,
    iniciate_number VARCHAR(255) NOT NULL,
    accept_number VARCHAR(255) NOT NULL,
    date_start TIMESTAMP NOT NULL,
    date_end TIMESTAMP NOT NULL
);

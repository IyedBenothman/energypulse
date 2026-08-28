CREATE TABLE customer(
    customer_id VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    PRIMARY KEY (customer_id)
);

CREATE SEQUENCE meter_reading_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE meter_reading(
    id BIGINT NOT NULL,
    meter_id VARCHAR(255),
    consumption_kwh DOUBLE PRECISION NOT NULL,
    customer_id VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_meter_reading_customer
        FOREIGN KEY (customer_id)
        REFERENCES customer(customer_id)
);
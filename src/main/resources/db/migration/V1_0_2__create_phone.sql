CREATE TABLE phones (
    id BIGSERIAL NOT NULL,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    amount INT NOT NULL,
    color VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE phones ADD CONSTRAINT phones_id UNIQUE (id);

INSERT INTO phones (id, brand, model, price, amount, color, description) VALUES
(1, 'Apple', 'iPhone 11', 600, 23, 'Black', 'Nice phone'),
(2, 'Apple', 'iPhone 11', 600, 52, 'Red', 'Nice phone'),
(3, 'Apple', 'iPhone 11', 600, 35, 'White', 'Nice phone'),
(4, 'Apple', 'iPhone X', 300, 10, 'Space grey', 'Nice phone'),
(5, 'Apple', 'iPhone 13 Pro Max', 1200, 40, 'White', 'Nice phone');

ALTER SEQUENCE phones_id_seq RESTART WITH 6;
CREATE TABLE baskets (
    id BIGSERIAL NOT NULL,
    user_id BIGINT NOT NULL,
    price FLOAT NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE baskets ADD CONSTRAINT baskets_id UNIQUE (id);

ALTER TABLE baskets
    ADD CONSTRAINT FK_BASKETS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

INSERT INTO baskets (id, user_id, price) VALUES
(1, 1, 2100),
(2, 2, 2700);

ALTER SEQUENCE baskets_id_seq RESTART WITH 3;



CREATE TABLE basket_phones (
    basket_id BIGINT NOT NULL,
    phone_id BIGINT NOT NULL,
    amount INT NOT NULL,
    PRIMARY KEY (basket_id, phone_id)
);

ALTER TABLE basket_phones
    ADD CONSTRAINT FK_BASKET_PHONES_ON_BASKET FOREIGN KEY (basket_id) REFERENCES baskets (id);

ALTER TABLE basket_phones
    ADD CONSTRAINT FK_BASKET_PHONES_ON_PHONE FOREIGN KEY (phone_id) REFERENCES phones (id);

INSERT INTO basket_phones (basket_id, phone_id, amount) VALUES
(1, 1, 4),
(1, 4, 1),
(2, 5, 1),
(2, 1, 2),
(2, 4, 1);
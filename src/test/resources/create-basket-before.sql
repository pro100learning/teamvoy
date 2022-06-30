INSERT INTO baskets (id, user_id, price) VALUES
(1, 1, 2100),
(2, 2, 2700);

ALTER SEQUENCE baskets_id_seq RESTART WITH 3;

INSERT INTO basket_phones (basket_id, phone_id, amount) VALUES
(1, 1, 4),
(1, 4, 1),
(2, 5, 1),
(2, 1, 2),
(2, 4, 1);
INSERT INTO orders (id, user_id, status, price, paid, created_at) VALUES
(1, 1, 'PAID', 1200, true, '2022-06-28 10:11:00.00'),
(2, 1, 'ARCHIVE', 3000, true, '2022-06-28 12:44:00.00'),
(3, 2, 'DELIVERED', 600, true, '2022-06-29 5:25:00.00'),
(4, 2, 'PAID', 1800, true, '2022-06-29 11:01:00.00'),
(5, 2, 'CREATED', 2700, false, '2022-06-30 1:10:00.00');

ALTER SEQUENCE orders_id_seq RESTART WITH 6;

INSERT INTO order_phones (order_id, phone_id, amount) VALUES
(1, 5, 1),
(2, 1, 3), (2, 5, 1),
(3, 4, 2),
(4, 1, 1), (4, 2, 1), (4, 3, 1),
(5, 1, 2), (5, 4, 1), (5, 5, 1);
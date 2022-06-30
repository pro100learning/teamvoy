INSERT INTO users (id, surname, name, email, phone, password, enabled) VALUES
(1, 'Ткачук', 'Богдан', 'test1@gmail.com', '380972553991', '$2a$10$aNwQFNT/GxmWNYaG9CZ4BuoMh65OdysRNie5gJqHxyzHwkrx7gB/6', true),
(2, 'Татарчук', 'Андрій', 'test2@gmail.com', '380972553992', '$2a$10$aNwQFNT/GxmWNYaG9CZ4BuoMh65OdysRNie5gJqHxyzHwkrx7gB/6', true);

INSERT INTO user_roles(user_id, role) VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_USER'),
(2, 'ROLE_MANAGER');

ALTER SEQUENCE users_id_seq RESTART WITH 3;
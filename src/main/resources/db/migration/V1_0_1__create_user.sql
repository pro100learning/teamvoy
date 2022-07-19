CREATE TABLE users (
    id BIGSERIAL NOT NULL,
    surname VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    password VARCHAR(64) NOT NULL,
    enabled BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE users ADD CONSTRAINT users_id UNIQUE (id);
ALTER TABLE users ADD CONSTRAINT users_email UNIQUE (email);
ALTER TABLE users ADD CONSTRAINT users_phone UNIQUE (phone);

INSERT INTO users (id, surname, name, email, phone, password, enabled) VALUES
(1, 'Ткачук', 'Богдан', 'test1@gmail.com', '380972553991', '$2a$10$aNwQFNT/GxmWNYaG9CZ4BuoMh65OdysRNie5gJqHxyzHwkrx7gB/6', true),
(2, 'Татарчук', 'Андрій', 'test2@gmail.com', '380972553992', '$2a$10$aNwQFNT/GxmWNYaG9CZ4BuoMh65OdysRNie5gJqHxyzHwkrx7gB/6', true);

ALTER SEQUENCE users_id_seq RESTART WITH 3;



CREATE TABLE user_roles (
    user_id int8 NOT NULL,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role)
);

ALTER TABLE user_roles ADD CONSTRAINT users_roles_user_id FOREIGN KEY (user_id) REFERENCES users;

INSERT INTO user_roles(user_id, role) VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_USER'),
(2, 'ROLE_MANAGER');
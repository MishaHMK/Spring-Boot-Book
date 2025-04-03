INSERT INTO users (id, email, password, first_name, last_name, shipping_address)
VALUES (1, 'user@gmail.com', '12345678', 'user', 'user', 'address');

INSERT INTO carts (id, user_id)
VALUES (1, 1);

INSERT INTO roles (id, role)
VALUES (1, 'ADMIN'),
       (2, 'USER');
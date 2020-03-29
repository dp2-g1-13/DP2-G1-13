INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Tennant', 'ababa', 1, 'AnonimO__1099', '20133699A', 'danielsanchezbaledasyron@hotmail.com', 'Daniel', 'Sanchez', '657346638', NULL);
INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Host', 'asasa', 1, 'AnonimO__1099', '20133699G', 'danielsanchezbaleyron@hotmail.com', 'Daniel', 'Sanchez', '657346638', NULL);
INSERT INTO authorities (username, authority) VALUES('ababa', 'TENNANT');
INSERT INTO authorities (username, authority) VALUES('asasa', 'HOST');

INSERT INTO messages (id, body, creation_moment, receiver_username, sender_username) VALUES(1, 'Hola', '2020-03-28 15:48:45.126', 'ababa', 'asasa');
INSERT INTO messages (id, body, creation_moment, receiver_username, sender_username) VALUES(2, 'Hola, ¿que tal?', '2020-03-28 15:49:05.332', 'asasa', 'ababa');
INSERT INTO messages (id, body, creation_moment, receiver_username, sender_username) VALUES(3, 'Bien, ¿Y tu?', '2020-03-28 15:49:11.521', 'asasa', 'ababa');
INSERT INTO messages (id, body, creation_moment, receiver_username, sender_username) VALUES(4, 'Bien', '2020-03-28 15:49:16.484', 'asasa', 'ababa');
INSERT INTO messages (id, body, creation_moment, receiver_username, sender_username) VALUES(5, 'Adios', '2020-03-28 15:49:20.617', 'asasa', 'ababa');
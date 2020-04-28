-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Tenant', 'tenant1', 1, 'Is-Dp2-G1-13', '98765432A', 'tenant1@alum.us.es', 'Daniel', 'Sanchez', '654321987', NULL);
INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Host', 'host1', 1, 'Is-Dp2-G1-13', '12345678B', 'host1@us.es', 'Ramon', 'Fernandez de la Rosa', '661707683', NULL);
INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Host', 'host2', 1, 'Is-Dp2-G1-13', '11122233W', 'host2@alum.us.es', 'Jorge', 'Raposo', '698741203', NULL);
INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Tenant', 'tenant2', 1, 'Is-Dp2-G1-13', '66655544A', 'tenant2@alum.us.es', 'Jose Ramon', 'Fernandez', '632014577', NULL);
INSERT INTO users(DTYPE,username,password,enabled) VALUES ('User','admin1','4dm1n',TRUE);

INSERT INTO authorities VALUES ('admin1','ADMIN');

INSERT INTO authorities (username, authority) VALUES('tenant1', 'TENANT');
INSERT INTO authorities (username, authority) VALUES('host1', 'HOST');
INSERT INTO authorities (username, authority) VALUES('host2', 'HOST');
INSERT INTO authorities (username, authority) VALUES('tenant2', 'TENANT');

INSERT INTO addresses (id, address, city, country, postal_code) VALUES(1, 'Calle Enramadilla', 'Sevilla', 'Spain', '41018');
INSERT INTO addresses (id, address, city, country, postal_code) VALUES(2, 'Paseo de la Castellana', 'Madrid', 'Spain', '28046');
INSERT INTO addresses (id, address, city, country, postal_code) VALUES(3, 'Calle Amparo', 'Sevilla', 'Spain', '41003');

INSERT INTO flats (id, available_services, description, number_baths, number_rooms, square_meters, address_id, host_id) VALUES(1, 'TV, WiFi, central heating system', 'From the outside this house looks very elegant. It has been built with grey stones and has cypress wooden decorations. Small, rounded windows allow enough light to enter the home and have been added to the house in a mostly symmetric way.

The house is equipped with a modern kitchen and two bathrooms, it also has a small living room, two bedrooms, a snug dining room and a modest storage room.

The building is shaped like a T. The two extensions extend into a garden path reaching until the end of that side of the house.
The second floor is smaller than the first, which allowed for several balconies on the sides of the house. This floor has a different style than the floor below.', 2, 2, 210, 1, 'host1');
INSERT INTO flats (id, available_services, description, number_baths, number_rooms, square_meters, address_id, host_id) VALUES(2, 'WiFi and central heating system', 'From the outside this house looks grandiose. It has been built with wood covered in render and has brown stone decorations. Tall, rectangular windows add to the overall style of the house and have been added to the house in a very symmetric way.

The house is equipped with a modern kitchen and one modern bathroom, it also has a comfortable living room, three bedrooms, a modest dining room, a playroom and a large garage.

The building is fairly rounded in shape. The house is half surrounded by overgrown wooden overhanging panels.
The second floor is bigger than the first, which creates a stylish overhang around half the house. This floor has a different style than the floor below.

The roof is high and square shaped and is covered with black roof tiles. Two large chimneys poke out the center of the roof. There are no windows on the roof.
The house itself is surrounded by a gorgeous garden, including hanging grape vines, a pagoda, a pond and many different flowers.', 2, 3, 126, 2, 'host2');
INSERT INTO flats (id, available_services, description, number_baths, number_rooms, square_meters, address_id, host_id) VALUES(3, 'Hot water, air conditioner, central heating system', 'From the outside this house looks impressive. It has been built with tan bricks and has fir wooden decorations. Tall, rounded windows allow enough light to enter the home and have been added to the house in a mostly asymmetric way.

The house is equipped with a small kitchen and one average bathroom, it also has a huge living room, four bedrooms, a cozy dining room, a game room and a cozy storage room.
The building is shaped like a short U. The two extensions extend into overgrown wooden overhanging panels to each side.
The second floor is smaller than the first, which allowed for several balconies on the sides of the house. This floor has roughly the same style as the floor below.

The roof is high and slanted to one side and is covered with brown roof tiles. Two small chimneys sit at either side of the house. A few large windows let in just enough light to the rooms below the roof.
The house itself is surrounded by a gorgeous garden, including hanging grape vines, a pagoda, a pond and many different flowers.', 1, 1, 46, 3, 'host1');

INSERT INTO images (id, data, file_type, filename) VALUES(1, FILE_READ('src/main/resources/static/resources/images/flats/30.-DLX-800x533.jpg'), 'image/jpg', '30.-DLX-800x533.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(2, FILE_READ('src/main/resources/static/resources/images/flats/450_1000.jpeg'), 'image/jpeg', '450_1000.jpeg');
INSERT INTO images (id, data, file_type, filename) VALUES(3, FILE_READ('src/main/resources/static/resources/images/flats/450_1000.jpg'), 'image/jpg', '450_1000.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(4, FILE_READ('src/main/resources/static/resources/images/flats/755093144.jpg'), 'image/jpg', '755093144.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(5, FILE_READ('src/main/resources/static/resources/images/flats/baño.jpg'), 'image/jpg', 'baño.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(6, FILE_READ('src/main/resources/static/resources/images/flats/h1_b.jpeg'), 'image/jpeg', 'h1_b.jpeg');

INSERT INTO images (id, data, file_type, filename) VALUES(7, FILE_READ('src/main/resources/static/resources/images/flats/36SMDeptford1809-3.jpg'), 'image/jpg', '36SMDeptford1809-3.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(8, FILE_READ('src/main/resources/static/resources/images/flats/1856146.jpg'), 'image/jpg', '1856146.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(9, FILE_READ('src/main/resources/static/resources/images/flats/a_fill.jpg'), 'image/jpg', 'a_fill.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(10, FILE_READ('src/main/resources/static/resources/images/flats/baño-diseño-moderno-32.jpg'), 'image/jpg', 'baño-diseño-moderno-32.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(11, FILE_READ('src/main/resources/static/resources/images/flats/DP18-147587_L1_KitchenCat_Update_hero_1-1_dt_2col_KitchenUpgrade-Default.jpg'), 'image/jpg', 'DP18-147587_L1_KitchenCat_Update_hero_1-1_dt_2col_KitchenUpgrade-Default.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(12, FILE_READ('src/main/resources/static/resources/images/flats/the-master-bedroom-extends-into-the-former-enclosed-balcony-with-a-wall-of-built-in-custom-cabinets.jpg'), 'image/jpg', 'the-master-bedroom-extends-into-the-former-enclosed-balcony-with-a-wall-of-built-in-custom-cabinets.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(13, FILE_READ('src/main/resources/static/resources/images/flats/287390453.jpg'), 'image/jpg', '287390453.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(14, FILE_READ('src/main/resources/static/resources/images/flats/287390458.jpg'), 'image/jpg', '287390458.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(15, FILE_READ('src/main/resources/static/resources/images/flats/287390490.jpg'), 'image/jpg', '287390490.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(16, FILE_READ('src/main/resources/static/resources/images/flats/287390534.jpg'), 'image/jpg', '287390534.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(17, FILE_READ('src/main/resources/static/resources/images/flats/287390541.jpg'), 'image/jpg', '287390541.jpg');
INSERT INTO images (id, data, file_type, filename) VALUES(18, FILE_READ('src/main/resources/static/resources/images/flats/287390549.jpg'), 'image/jpg', '287390549.jpg');


INSERT INTO flats_images (flat_id, images_id) VALUES(1, 1);
INSERT INTO flats_images (flat_id, images_id) VALUES(1, 2);
INSERT INTO flats_images (flat_id, images_id) VALUES(1, 3);
INSERT INTO flats_images (flat_id, images_id) VALUES(1, 4);
INSERT INTO flats_images (flat_id, images_id) VALUES(1, 5);
INSERT INTO flats_images (flat_id, images_id) VALUES(1, 6);
INSERT INTO flats_images (flat_id, images_id) VALUES(2, 7);
INSERT INTO flats_images (flat_id, images_id) VALUES(2, 8);
INSERT INTO flats_images (flat_id, images_id) VALUES(2, 9);
INSERT INTO flats_images (flat_id, images_id) VALUES(2, 10);
INSERT INTO flats_images (flat_id, images_id) VALUES(2, 11);
INSERT INTO flats_images (flat_id, images_id) VALUES(2, 12);
INSERT INTO flats_images (flat_id, images_id) VALUES(3, 13);
INSERT INTO flats_images (flat_id, images_id) VALUES(3, 14);
INSERT INTO flats_images (flat_id, images_id) VALUES(3, 15);
INSERT INTO flats_images (flat_id, images_id) VALUES(3, 16);
INSERT INTO flats_images (flat_id, images_id) VALUES(3, 17);
INSERT INTO flats_images (flat_id, images_id) VALUES(3, 18);

INSERT INTO advertisements (id, creation_date, description, price_per_month, requirements, title, flat_id) VALUES(1, '2020-03-26', 'I offer a modern, beautiful flat in the centre of Seville, next to an underground station, a tram stop and a commuter train station.', 850.9, 'There are no requirements for this flat.', 'Beautiful flat in San Bernardo', 1);
INSERT INTO advertisements (id, creation_date, description, price_per_month, requirements, title, flat_id) VALUES(2, '2020-03-26', 'I rent a flat in Paseo de la Castellana. It is an excellent centrical and luminous one.', 1650, 'There are no requirements for this flat.', 'Centrical flat in Madrid', 2);
INSERT INTO advertisements (id, creation_date, description, price_per_month, requirements, title, flat_id) VALUES(3, '2020-03-27', 'I rent a beautiful apartment in the centre of Seville, in Encarnación-Regina', 670.95, 'There are no requirements for this flat.', 'Beautiful apartment in Encarnación-Regina', 3);

INSERT INTO requests (id, creation_date, description, finish_date, start_date, status, advertisement_id) VALUES(1, '2020-03-27 18:42:27.109905', 'This is sample request', '2021-12-30', '2020-12-31', 'PENDING', 1);
INSERT INTO requests (id, creation_date, description, finish_date, start_date, status, advertisement_id) VALUES(2, '2020-03-27 18:43:04.920847', 'This is another sample request', '2021-07-30', '2020-09-30', 'PENDING', 3);
INSERT INTO requests (id, creation_date, description, finish_date, start_date, status, advertisement_id) VALUES(3, '2020-03-27 18:44:05.251223', 'This is a sample request from tenant2.', '2020-12-30', '2020-10-31', 'PENDING', 1);
INSERT INTO requests (id, creation_date, description, finish_date, start_date, status, advertisement_id) VALUES(4, '2020-03-27 19:00:34.521884', 'Sample description of request.', '2020-08-29', '2020-03-28', 'ACCEPTED', 2);
INSERT INTO requests (id, creation_date, description, finish_date, start_date, status, advertisement_id) VALUES(5, '2020-03-27 19:01:07.185945', 'Sample description of tenant4', '2020-11-28', '2020-03-28', 'ACCEPTED', 2);

INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Tenant', 'tenant3', 1, 'Is-Dp2-G1-13', '36954820X', 'tenant3@us.es', 'Tenant', 'Three', '820314698', 2);
INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Tenant', 'tenant4', 1, 'Is-Dp2-G1-13', '45688700Q', 'tenant4@us.es', 'Tenant', 'Four', '999888777', 2);

INSERT INTO authorities (username, authority) VALUES('tenant3', 'TENANT');
INSERT INTO authorities (username, authority) VALUES('tenant4', 'TENANT');

INSERT INTO users_requests (tenant_username, requests_id) VALUES('tenant1', 1);
INSERT INTO users_requests (tenant_username, requests_id) VALUES('tenant1', 2);
INSERT INTO users_requests (tenant_username, requests_id) VALUES('tenant2', 3);
INSERT INTO users_requests (tenant_username, requests_id) VALUES('tenant3', 4);
INSERT INTO users_requests (tenant_username, requests_id) VALUES('tenant4', 5);

INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Host', 'jfdelarosa', 1, 'Is-Dp2-G1-13', '28983254B', 'jfdelarosa@us.es', 'Ramon', 'Fernandez de la Rosa', '661707683', NULL);
INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Host', 'jorrapdia', 1, 'Is-Dp2-G1-13', '96325874W', 'jorrapdia@alum.us.es', 'Jorge', 'Raposo', '698741203', NULL);
INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Tenant', 'dansanbal', 1, 'Is-Dp2-G1-13', '20015860A', 'dansanbal@alum.us.es', 'Daniel', 'Sanchez', '654321987', 1);
INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Tenant', 'josferde5', 1, 'Is-Dp2-G1-13', '11111111A', 'josferde5@alum.us.es', 'Jose Ramon', 'Fernandez', '632014577', 1);
INSERT INTO authorities (username, authority) VALUES('dansanbal', 'TENANT');
INSERT INTO authorities (username, authority) VALUES('jfdelarosa', 'HOST');
INSERT INTO authorities (username, authority) VALUES('jorrapdia', 'HOST');
INSERT INTO authorities (username, authority) VALUES('josferde5', 'TENANT');
INSERT INTO reviews (dtype, id, creation_date, description, rate, creator_username, flat_id) VALUES ('FlatReview', 1, '2020-03-29', 'description', 2, 'dansanbal', 1);
INSERT INTO reviews (dtype, id, creation_date, description, rate, creator_username, flat_id) VALUES ('TenantReview', 2, '2020-03-29', 'description', 3, 'josferde5', null);
INSERT INTO users_reviews (tenant_username, reviews_id) VALUES ('dansanbal', 2);
INSERT INTO reports (id, creation_date, reason, receiver_username, sender_username) VALUES (1, '2020-03-29', 'reason', 'dansanbal', 'josferde5');
INSERT INTO tasks (id, creation_date, description, status, title, asignee_username, creator_username) VALUES (1, '2020-03-29', 'description', 'TODO', 'title', 'dansanbal', 'josferde5');

INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Tenant', 'ababa', 1, 'AnonimO__1099', '20133699A', 'danielsanchezbaledasyron@hotmail.com', 'Daniel', 'Sanchez', '657346638', NULL);
INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Host', 'asasa', 1, 'AnonimO__1099', '20133699G', 'danielsanchezbaleyron@hotmail.com', 'Daniel', 'Sanchez', '657346638', NULL);
INSERT INTO authorities (username, authority) VALUES('ababa', 'TENANT');
INSERT INTO authorities (username, authority) VALUES('asasa', 'HOST');

INSERT INTO messages (id, body, creation_moment, receiver_username, sender_username) VALUES(1, 'Hola', '2020-03-28 15:48:45.126', 'ababa', 'asasa');
INSERT INTO messages (id, body, creation_moment, receiver_username, sender_username) VALUES(2, 'Hola, ¿que tal?', '2020-03-28 15:49:05.332', 'asasa', 'ababa');
INSERT INTO messages (id, body, creation_moment, receiver_username, sender_username) VALUES(3, 'Bien, ¿Y tu?', '2020-03-28 15:49:11.521', 'asasa', 'ababa');
INSERT INTO messages (id, body, creation_moment, receiver_username, sender_username) VALUES(4, 'Bien', '2020-03-28 15:49:16.484', 'asasa', 'ababa');
INSERT INTO messages (id, body, creation_moment, receiver_username, sender_username) VALUES(5, 'Adios', '2020-03-28 15:49:20.617', 'asasa', 'ababa');

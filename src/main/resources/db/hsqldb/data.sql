-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(DTYPE,username,password,enabled) VALUES ('Admin','admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');
INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Host', 'jfdelarosa', 1, 'Is-Dp2-G1-13', '28983254B', 'jfdelarosa@us.es', 'Ramon', 'Fernandez de la Rosa', '661707683', NULL);
INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Host', 'jorrapdia', 1, 'Is-Dp2-G1-13', '96325874W', 'jorrapdia@alum.us.es', 'Jorge', 'Raposo', '698741203', NULL);
INSERT INTO addresses (id, address, city, country, postal_code) VALUES(1, 'Calle Enramadilla', 'Sevilla', 'Spain', '41018');
INSERT INTO addresses (id, address, city, country, postal_code) VALUES(2, 'Paseo de la Castellana', 'Madrid', 'Spain', '28046');
INSERT INTO flats (id, available_services, description, number_baths, number_rooms, square_meters, address_id, host_id) VALUES(1, 'TV, WiFi, central heating system', 'From the outside this house looks very elegant. It has been built with grey stones and.', 2, 2, 210, 1, 'jfdelarosa');
INSERT INTO flats (id, available_services, description, number_baths, number_rooms, square_meters, address_id, host_id) VALUES(2, 'WiFi and central heating system', 'From the outside this house looks grandiose. It has been built with wood covered in render and has.', 2, 3, 126, 2, 'jorrapdia');
INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Tennant', 'dansanbal', 1, 'Is-Dp2-G1-13', '20015860A', 'dansanbal@alum.us.es', 'Daniel', 'Sanchez', '654321987', 1);
INSERT INTO users (dtype, username, enabled, password, dni, email, first_name, last_name, phone_number, flat_id) VALUES('Tennant', 'josferde5', 1, 'Is-Dp2-G1-13', '11111111A', 'josferde5@alum.us.es', 'Jose Ramon', 'Fernandez', '632014577', 1);
INSERT INTO authorities (username, authority) VALUES('dansanbal', 'TENNANT');
INSERT INTO authorities (username, authority) VALUES('jfdelarosa', 'HOST');
INSERT INTO authorities (username, authority) VALUES('jorrapdia', 'HOST');
INSERT INTO authorities (username, authority) VALUES('josferde5', 'TENNANT');
INSERT INTO review (dtype, id, creation_date, description, rate, creator_username, flat_id) VALUES ('FlatReview', 1, '2020-03-29', 'description', 2, 'dansanbal', 1);
INSERT INTO review (dtype, id, creation_date, description, rate, creator_username, flat_id) VALUES ('TennantReview', 2, '2020-03-29', 'description', 3, 'josferde5', null);
-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(DTYPE,username,password,enabled) VALUES ('Admin','admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');

INSERT INTO users(DTYPE,username,password,enabled) VALUES ('Host','jfdelarosa','jfdelarosa',TRUE);
INSERT INTO authorities VALUES ('jfdelarosa','HOST');

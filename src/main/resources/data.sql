
ALTER TABLE dbuser ADD FOREIGN KEY (role_id) REFERENCES role (id);
ALTER TABLE dbuser ADD FOREIGN KEY (address) REFERENCES address (id);
CREATE UNIQUE INDEX dbuser_email_uindex ON dbuser (email);
ALTER TABLE verification_token ADD FOREIGN KEY (user_id) REFERENCES dbuser (id);
CREATE UNIQUE INDEX verification_token_user_id_uindex ON verification_token (user_id);
CREATE UNIQUE INDEX verification_token_token_uindex ON verification_token (token);

INSERT INTO role (name) VALUES ('CLIENT');
INSERT INTO role (name) VALUES ('ADMIN');
INSERT INTO role (name) VALUES ('CSR');
INSERT INTO role (name) VALUES ('PMG');

INSERT INTO address (country, region, settlement, street, house_number, apartment) VALUES ('Ukrain', 'Kiev', 'Kiev', 'Yangela St.', '16/2', '310');
INSERT INTO address (country, region, settlement, street, house_number, apartment) VALUES ('Ukrain', 'Dnipro', 'Dnipro', 'Naberezhna St.', '15', null);
INSERT INTO address (country, region, settlement, street, house_number, apartment) VALUES ('Ukrain', 'Dnipro', 'Kamyanske', 'Zarichna St.', '22', '2');
INSERT INTO address (country, region, settlement, street, house_number, apartment) VALUES ('Ukrain', 'Kiev', 'Bila cerkva', 'Michurina St.', '17', null);
INSERT INTO address (country, region, settlement, street, house_number, apartment) VALUES ('Ukrain', 'Kiev', 'Kiev', 'Peremohy St.', '2', '16');
INSERT INTO address (country, region, settlement, street, house_number, apartment) VALUES ('Ukrain', 'Kharkiv', 'Kharkiv', 'Yangela St.', '22', '13');
INSERT INTO address (country, region, settlement, street, house_number, apartment) VALUES ('Ukrain', 'Lviv', 'Lviv', 'Sadova St.', '16', '2');
INSERT INTO address (country, region, settlement, street, house_number, apartment) VALUES ('Ukrain', 'Lviv', 'Drohobych', 'Lisova St.', '7', '14');

INSERT INTO dbuser (email, password, role_id, firstname, secondname, lastname, phone, address, state, username) VALUES ('testCSR1@mail.com', 'csr1', 3, 'CSR1FirstName', 'CSR1SecondName', 'CSR1LastName', '033-33-333-33', 4, null, null);
INSERT INTO dbuser (email, password, role_id, firstname, secondname, lastname, phone, address, state, username) VALUES ('testCSR2@mail.com', 'csr2', 3, 'CSR2FirstName', 'CSR2SecondName', 'CSR2LastName', '033-33-333-33', 5, null, null);
INSERT INTO dbuser (email, password, role_id, firstname, secondname, lastname, phone, address, state, username) VALUES ('testPMG1@mail.com', 'pmg1', 4, 'PMG1FirstName', 'PMG1SecondName', 'PMG1LastName', '033-33-333-33', 6, null, null);
INSERT INTO dbuser (email, password, role_id, firstname, secondname, lastname, phone, address, state, username) VALUES ('testPMG2@mail.com', 'pmg2', 4, 'PMG2FirstName', 'PMG2SecondName', 'PMG2LastName', '033-33-333-33', 7, null, null);
INSERT INTO dbuser (email, password, role_id, firstname, secondname, lastname, phone, address, state, username) VALUES ('testuser2@mail.com', 'client2', 1, 'testFirst2Name', 'testUset2SecondName', 'testUser2LastName', '022-22-22-22', 2, null, null);
INSERT INTO dbuser (email, password, role_id, firstname, secondname, lastname, phone, address, state, username) VALUES ('testPMG3@mail.com', 'pmg3', 4, 'PMG3FirstName', 'PMG3SecondName', 'PMG3LastName', '033-33-333-33', 7, null, null);
INSERT INTO dbuser (email, password, role_id, firstname, secondname, lastname, phone, address, state, username) VALUES ('testuser1@mail.com', '5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', 1, 'testFirstName', 'testUsetSecondName', 'Yurii', '011-11-111-11', 1, null, 'testuser1@mail.com');
INSERT INTO dbuser (email, password, role_id, firstname, secondname, lastname, phone, address, state, username) VALUES ('qweqw@MJASF', 'password;ljlkqweqweqwe', null, null, null, null, null, null, null, null);
INSERT INTO dbuser (email, password, role_id, firstname, secondname, lastname, phone, address, state, username) VALUES ('admin@mail.com', 'dc76e9f0c0006e8f919e0c515c66dbba3982f785', 2, 'jim', 'root', 'root', '033-33-333-33', 3, null, 'admin@mail.com');
INSERT INTO dbuser (email, password, role_id, firstname, secondname, lastname, phone, address, state, username) VALUES ('d.vodotiiets@yandex.ua', '7332ltt8bi', 2, null, null, null, null, 3, null, null);
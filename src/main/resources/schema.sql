DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS dbuser;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS verification_token;

DROP SEQUENCE IF EXISTS adrress_id_seq;
DROP SEQUENCE IF EXISTS dbuser_id_seq;
DROP SEQUENCE IF EXISTS role_id_seq;
DROP SEQUENCE IF EXISTS verification_token_id_seq;

CREATE SEQUENCE adrress_id_seq START WITH 1;
CREATE SEQUENCE dbuser_id_seq START WITH 1;
CREATE SEQUENCE role_id_seq START WITH 1;
CREATE SEQUENCE verification_token_id_seq START WITH 1;

CREATE TABLE address
(
  address_id       INTEGER DEFAULT nextval('adrress_id_seq') PRIMARY KEY NOT NULL,
  country          VARCHAR(255),
  region           VARCHAR(255),
  locality         VARCHAR(255),
  street           VARCHAR(255),
  house_number     VARCHAR(255),
  apartment_number VARCHAR(255)
);

CREATE TABLE dbuser
(
  id                   INTEGER DEFAULT nextval('dbuser_id_seq') PRIMARY KEY NOT NULL,
  email                VARCHAR(255),
  password             VARCHAR(255),
  role_id              INTEGER,
  firstname            VARCHAR(255),
  secondname           VARCHAR(255),
  lastname             VARCHAR(255),
  phone                VARCHAR(64),
  address_id           INTEGER,
  corporate_id         INTEGER,
  is_representative    BOOLEAN,
  customer_category_id INTEGER,
  status               VARCHAR(55),
  tariff_id            INTEGER
);

CREATE TABLE role
(
  id   INTEGER DEFAULT nextval('role_id_seq') PRIMARY KEY NOT NULL,
  name VARCHAR(100)
);

CREATE TABLE verification_token
(
  id          INTEGER DEFAULT nextval('verification_token_id_seq') PRIMARY KEY NOT NULL,
  user_id     INTEGER,
  token       VARCHAR(255),
  expiry_date DATE
);


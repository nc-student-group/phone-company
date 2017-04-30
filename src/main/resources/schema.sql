/*
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

CREATE TABLE IF NOT EXISTS public."order"
(
    id SERIAL PRIMARY KEY NOT NULL,
    customer_service_id INT,
    customer_tariff_id INT,
    type VARCHAR(255),
    order_status VARCHAR(255),
    creation_date DATE,
    execution_date DATE,
    CONSTRAINT order_customer_service_id_fk FOREIGN KEY (customer_service_id) REFERENCES customer_service (id),
    CONSTRAINT order_customer_tariff_id_fk FOREIGN KEY (customer_tariff_id) REFERENCES customer_tariff (id)
);

CREATE TABLE IF NOT EXISTS public.marketing_campaign
(
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255),
    marketing_campaign_status VARCHAR(255),
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS public.marketing_campaign_services
(
    id SERIAL PRIMARY KEY NOT NULL,
    marketing_campaign_id INT,
    price DOUBLE PRECISION,
    service_id INT,
    CONSTRAINT marketing_campaign_services_marketing_campaign_id_fk FOREIGN KEY (marketing_campaign_id) REFERENCES marketing_campaign (id),
    CONSTRAINT marketing_campaign_services_service_id_fk FOREIGN KEY (service_id) REFERENCES service (id)
);

CREATE TABLE IF NOT EXISTS public.marketing_campaign_tariff
(
    id SERIAL PRIMARY KEY NOT NULL,
    marketing_campaign_id INT,
    tariff_region_id INT,
    CONSTRAINT marketing_campaign_tariff_tariff_region_id_fk FOREIGN KEY (tariff_region_id) REFERENCES tariff_region (id),
    CONSTRAINT marketing_campaign_tariff_marketing_campaign_id_fk FOREIGN KEY (marketing_campaign_id) REFERENCES marketing_campaign (id)
);
*/

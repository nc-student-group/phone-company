-- Test Data
-- Region
INSERT INTO region(name_region) VALUES ('Cherkasy Oblast');
INSERT INTO region(name_region) VALUES ('Chernihiv Oblast');
INSERT INTO region(name_region) VALUES ('Chernivtsi Oblast');
INSERT INTO region(name_region) VALUES ('Dnipropetrovsk Oblast');
INSERT INTO region(name_region) VALUES ('Donetsk Oblast');
INSERT INTO region(name_region) VALUES ('Ivano-Frankivsk Oblast');
INSERT INTO region(name_region) VALUES ('Kharkiv Oblast');
INSERT INTO region(name_region) VALUES ('Kherson Oblast');
INSERT INTO region(name_region) VALUES ('Khmelnytskyi Oblast');
INSERT INTO region(name_region) VALUES ('Kiev Oblast');
INSERT INTO region(name_region) VALUES ('Kirovohrad Oblast');
INSERT INTO region(name_region) VALUES ('Luhansk Oblast');
INSERT INTO region(name_region) VALUES ('Lviv Oblast');
INSERT INTO region(name_region) VALUES ('Mykolaiv Oblast');
INSERT INTO region(name_region) VALUES ('Odessa Oblast');
INSERT INTO region(name_region) VALUES ('Poltava Oblast');
INSERT INTO region(name_region) VALUES ('Rivne Oblast');
INSERT INTO region(name_region) VALUES ('Sumy Oblast');
INSERT INTO region(name_region) VALUES ('Ternopil Oblast');
INSERT INTO region(name_region) VALUES ('Vinnytsia Oblast');
INSERT INTO region(name_region) VALUES ('Volyn Oblast');
INSERT INTO region(name_region) VALUES ('Zakarpattia Oblast');
INSERT INTO region(name_region) VALUES ('Zaporizhia Oblast');
INSERT INTO region(name_region) VALUES ('Zhytomyr Oblast');
INSERT INTO region(name_region) VALUES ('Crimea');

-- Address
INSERT INTO address(region_id, locality, street, house_number, apartment_number)
VALUES ('10', 'Kiev', 'Yangela St.', '16/2', '310');
INSERT INTO address(region_id, locality, street, house_number, apartment_number)
VALUES ('4', 'Dnipro', 'Naberezhna St.', '15', null);
INSERT INTO address(region_id, locality, street, house_number, apartment_number)
VALUES ('4', 'Kamyanske', 'Zarichna St.', '22A', '2');
INSERT INTO address(region_id, locality, street, house_number, apartment_number)
VALUES ('10', 'Bila cerkva', 'Michurina St.', '17', null);
INSERT INTO address(region_id, locality, street, house_number, apartment_number)
VALUES ('10','Kiev', 'Peremohy St.', '2', '16');
INSERT INTO address(region_id, locality, street, house_number, apartment_number)
VALUES ('7', 'Kharkiv', 'Yangela St.', '22', '13');
INSERT INTO address(region_id, locality, street, house_number, apartment_number)
VALUES ('13',  'Lviv', 'Sadova St.', '16', '2');
INSERT INTO address(region_id, locality, street, house_number, apartment_number)
VALUES ('13', 'Drohobych', 'Lisova St.', '7', '14');
INSERT INTO address(region_id, locality, street, house_number, apartment_number)
VALUES ('10', 'Kiev', 'Metalistiv St.', '1', '11');
INSERT INTO address(region_id, locality, street, house_number, apartment_number)
VALUES ('10', 'Kiev', 'Metalistiv St.', '2', '20');
INSERT INTO address(region_id, locality, street, house_number, apartment_number)
VALUES ('10', 'Kiev', 'Metalistiv St.', '3', '5');
INSERT INTO address(region_id, locality, street, house_number, apartment_number)
VALUES ('10', 'Kiev', 'Metalistiv St.', '3', '6');

-- Tariff
INSERT INTO tariff(tariff_name, product_status, internet, calls_in_network, calls_on_other_numbers, sms, mms, roaming, is_corporate)
VALUES ('Smartphone 3G+ Small', 'ACTIVATED', '0.75 GB', 'Unlimited', '1 uah/ 1 min', '40', '20', '1 uah / min', 'false');
INSERT INTO tariff(tariff_name, product_status, internet, calls_in_network, calls_on_other_numbers, sms, mms, roaming, is_corporate)
VALUES ('Smartphone 3G+ Medium', 'ACTIVATED', '2 GB', 'Unlimited', '0.5 uah/ 1 min', '60', '30', '0.5 uah / min', 'false');
INSERT INTO tariff(tariff_name, product_status, internet, calls_in_network, calls_on_other_numbers, sms, mms, roaming, is_corporate)
VALUES ('Smartphone 3G+ Large', 'ACTIVATED', '4 GB', 'Unlimited', '0.25 uah/ 1 min', '70', '40', '0.5 uah / min', 'false');
INSERT INTO tariff(tariff_name, product_status, internet, calls_in_network, calls_on_other_numbers, sms, mms, roaming, is_corporate)
VALUES ('Universal S', 'ACTIVATED', '2 GB', 'Unlimited', '200 min', '200', '100', '1 uah / min', 'true');
INSERT INTO tariff(tariff_name, product_status, internet, calls_in_network, calls_on_other_numbers, sms, mms, roaming, is_corporate)
VALUES ('Universal M', 'ACTIVATED', '5 GB', 'Unlimited', '400 min', '1000', '500', '0.5 uah / min', 'true');
INSERT INTO tariff(tariff_name, product_status, internet, calls_in_network, calls_on_other_numbers, sms, mms, roaming, is_corporate)
VALUES ('Universal L', 'ACTIVATED', '8 GB', 'Unlimited', '400 min', '1500', '700', '0.2 uah / min', 'true');

-- Tariff Region
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (1, 1, 40);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (1, 2, 60);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (1, 3, 80);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (1, 4, 60);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (1, 5, 120);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (1, 6, 170);

INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (4, 1, 40);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (4, 2, 60);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (4, 3, 80);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (4, 4, 60);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (4, 5, 120);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (4, 6, 170);

INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (7, 1, 40);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (7, 2, 60);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (7, 3, 80);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (7, 4, 60);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (7, 5, 120);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (7, 6, 170);

INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (10, 1, 40);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (10, 2, 50);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (10, 3, 60);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (10, 4, 50);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (10, 5, 100);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (10, 6, 150);

INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (13, 1, 40);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (13, 2, 60);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (13, 3, 80);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (13, 4, 60);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (13, 5, 120);
INSERT INTO tariff_region(region_id, tariff_id, price) VALUES (13, 6, 170);

-- Product Category
INSERT INTO product_category(name_category, units) VALUES ('Internet package', 'GB');
INSERT INTO product_category(name_category, units) VALUES ('SMS/MMS', '');
INSERT INTO product_category(name_category, units) VALUES ('Useful services', '');

-- Service
INSERT INTO service(prod_category_id, service_name, price, product_status, discount)
VALUES ('1', '3G ONLINE 500GB', '25', 'ACTIVATED', '1');
INSERT INTO service(prod_category_id, service_name, price, product_status, discount)
VALUES ('1', '3G ONLINE 1GB', '30', 'ACTIVATED', '1');
INSERT INTO service(prod_category_id, service_name, price, product_status, discount)
VALUES ('1', '3G ONLINE 2GB', '39', 'ACTIVATED', '1');
INSERT INTO service(prod_category_id, service_name, price, product_status, discount)
VALUES ('1', '3G ONLINE 0.5GB', '25', 'DEACTIVATED', '1');
INSERT INTO service(prod_category_id, service_name, price, product_status, discount)
VALUES ('1', '3G ONLINE 10GB', '39', 'ACTIVATED', '0.8');

INSERT INTO service(prod_category_id, service_name, price, product_status, discount)
VALUES ('2', '100 SMS', '15', 'ACTIVATED', '1');
INSERT INTO service(prod_category_id, service_name, price, product_status, discount)
VALUES ('2', '250 SMS', '35', 'ACTIVATED', '1');
INSERT INTO service(prod_category_id, service_name, price, product_status, discount)
VALUES ('2', '500 SMS', '50', 'ACTIVATED', '1');
INSERT INTO service(prod_category_id, service_name, price, product_status, discount)
VALUES ('2', 'Bulk SMS', '100', 'ACTIVATED', '1');

INSERT INTO service(prod_category_id, service_name, price, product_status, discount)
VALUES ('3', 'Calling Line Identification Restriction (CLIR)', '21', 'ACTIVATED', '1');
INSERT INTO service(prod_category_id, service_name, price, product_status, discount)
VALUES ('3', 'Hidden number identifier', '30', 'ACTIVATED', '1');
INSERT INTO service(prod_category_id, service_name, price, product_status, discount)
VALUES ('3', 'Meloring', '15', 'ACTIVATED', '1');

-- Corporate
INSERT INTO corporate(corporate_name) VALUES ('Netcracker Technology');
INSERT INTO corporate(corporate_name) VALUES ('KPI Telecom');

-- Customer
INSERT INTO dbuser(email, password, role_id, firstname, secondname, lastname,
                   phone, address_id, corporate_id, is_representative, status)
VALUES ('testClient1@mail.com', 'dc76e9f0c0006e8f919e0c515c66dbba3982f785', '4', 'Fname1', 'Sname1', 'Lname1', '0773816721', '1', NULL, 'false', 'ACTIVATED');
INSERT INTO dbuser(email, password, role_id, firstname, secondname, lastname,
                   phone, address_id, corporate_id, is_representative, status)
VALUES ('testClient2@mail.com', 'dc76e9f0c0006e8f919e0c515c66dbba3982f785', '4', 'Fname2', 'Sname2', 'Lname2', '0773816722', '2', NULL, 'false', 'ACTIVATED');
INSERT INTO dbuser(email, password, role_id, firstname, secondname, lastname,
                   phone, address_id, corporate_id, is_representative, status)
VALUES ('testClient3@mail.com', 'dc76e9f0c0006e8f919e0c515c66dbba3982f785', '4', 'Fname3', 'Sname3', 'Lname3', '0773816723', '3', NULL, 'false', 'ACTIVATED');

INSERT INTO dbuser(email, password, role_id, firstname, secondname, lastname,
                   phone, address_id, corporate_id, is_representative, status)
VALUES ('testCorpClient1@mail.com', 'dc76e9f0c0006e8f919e0c515c66dbba3982f785', '4', 'Fname4', 'Sname4', 'Lname4', '0773816724', '4', '1', 'false', 'ACTIVATED');
INSERT INTO dbuser(email, password, role_id, firstname, secondname, lastname,
                   phone, address_id, corporate_id, is_representative, status)
VALUES ('testCorpClient2@mail.com', 'dc76e9f0c0006e8f919e0c515c66dbba3982f785', '4', 'Fname5', 'Sname5', 'Lname5', '0773816725', '5', '1', 'false', 'ACTIVATED');
INSERT INTO dbuser(email, password, role_id, firstname, secondname, lastname,
                   phone, address_id, corporate_id, is_representative, status)
VALUES ('testCorpClient3@mail.com', 'dc76e9f0c0006e8f919e0c515c66dbba3982f785', '4', 'Fname6', 'Sname6', 'Lname6', '0773816726', '6', '1', 'true', 'ACTIVATED');
INSERT INTO dbuser(email, password, role_id, firstname, secondname, lastname,
                   phone, address_id, corporate_id, is_representative, status)
VALUES ('testCorpClient4@mail.com', 'dc76e9f0c0006e8f919e0c515c66dbba3982f785', '4', 'Fname7', 'Sname7', 'Lname7', '0773816727', '7', '2', 'false', 'ACTIVATED');
INSERT INTO dbuser(email, password, role_id, firstname, secondname, lastname,
                   phone, address_id, corporate_id, is_representative, status)
VALUES ('testCorpClient5@mail.com', 'dc76e9f0c0006e8f919e0c515c66dbba3982f785', '4', 'Fname8', 'Sname8', 'Lname8', '0773816728', '8', '2', 'true', 'ACTIVATED');

-- User
INSERT INTO dbuser(email, password, role_id, status)
VALUES ('admin@mail.com', 'dc76e9f0c0006e8f919e0c515c66dbba3982f785','1', 'ACTIVATED');
INSERT INTO dbuser(email, password, role_id, status)
VALUES ('csr1@mail.com', 'dc76e9f0c0006e8f919e0c515c66dbba3982f785','2', 'ACTIVATED');
INSERT INTO dbuser(email, password, role_id, status)
VALUES ('csr2@mail.com', 'dc76e9f0c0006e8f919e0c515c66dbba3982f785','2', 'ACTIVATED');
INSERT INTO dbuser(email, password, role_id, status)
VALUES ('pmg1@mail.com', 'dc76e9f0c0006e8f919e0c515c66dbba3982f785','3', 'ACTIVATED');
INSERT INTO dbuser(email, password, role_id, status)
VALUES ('pmg2@mail.com', 'dc76e9f0c0006e8f919e0c515c66dbba3982f785','3', 'ACTIVATED');

-- Customer Tariff
INSERT INTO customer_tariff(customer_id, corporate_id, order_date, total_price, order_status,tariff_id)
VALUES ('53', NULL, to_date('2016-09-01', 'YYYY-MM-DD'), '30', 'ACTIVE', '1');
INSERT INTO customer_tariff(customer_id, corporate_id, order_date, total_price, order_status,tariff_id)
VALUES ('54', NULL, to_date('2016-10-01', 'YYYY-MM-DD'), '60', 'DEACTIVATED', '2');
INSERT INTO customer_tariff(customer_id, corporate_id, order_date, total_price, order_status,tariff_id)
VALUES ('55', NULL, to_date('2016-11-12', 'YYYY-MM-DD'), '80', 'SUSPENDED', '3');

INSERT INTO customer_tariff(customer_id, corporate_id, order_date, total_price, order_status,tariff_id)
VALUES ('56', '1', to_date('2016-03-01', 'YYYY-MM-DD'), '120', 'ACTIVE', '5');
INSERT INTO customer_tariff(customer_id, corporate_id, order_date, total_price, order_status,tariff_id)
VALUES ('57', '1', to_date('2016-03-01', 'YYYY-MM-DD'), '120', 'ACTIVE', '5');
INSERT INTO customer_tariff(customer_id, corporate_id, order_date, total_price, order_status,tariff_id)
VALUES ('58', '1', to_date('2017-03-15', 'YYYY-MM-DD'), '120', 'ACTIVE', '5');
INSERT INTO customer_tariff(customer_id, corporate_id, order_date, total_price, order_status,tariff_id)
VALUES ('59', '2', to_date('2017-03-18', 'YYYY-MM-DD'), '170', 'ACTIVE', '6');
INSERT INTO customer_tariff(customer_id, corporate_id, order_date, total_price, order_status,tariff_id)
VALUES ('60', '2', to_date('2017-03-18', 'YYYY-MM-DD'), '60', 'DEACTIVATED', '4');

-- Customer service
INSERT INTO customer_service(customer_id, service_id, order_date, price, order_status)
VALUES ('53', '2', to_date('2017-03-18', 'YYYY-MM-DD'), '30', 'ACTIVE');
INSERT INTO customer_service(customer_id, service_id, order_date, price, order_status)
VALUES ('53', '2', to_date('2017-03-18', 'YYYY-MM-DD'), '30', 'SUSPENDED');
INSERT INTO customer_service(customer_id, service_id, order_date, price, order_status)
VALUES ('54', '3', to_date('2017-03-19', 'YYYY-MM-DD'), '39', 'ACTIVE');
INSERT INTO customer_service(customer_id, service_id, order_date, price, order_status)
VALUES ('55', '6', to_date('2017-03-20', 'YYYY-MM-DD'), '15', 'ACTIVE');
INSERT INTO customer_service(customer_id, service_id, order_date, price, order_status)
VALUES ('56', '11', to_date('2017-03-20', 'YYYY-MM-DD'), '30', 'ACTIVE');
INSERT INTO customer_service(customer_id, service_id, order_date, price, order_status)
VALUES ('57', '11', to_date('2017-03-20', 'YYYY-MM-DD'), '30', 'DEACTIVATED');

-- Complaint
INSERT INTO complaint(status, date, text, type, user_id)
VALUES ('', to_date('2017-03-25', 'YYYY-MM-DD'), 'Complaint1', 'type1', '53');
INSERT INTO complaint(status, date, text, type, user_id)
VALUES ('', to_date('2017-03-25', 'YYYY-MM-DD'), 'Complaint2', 'type2', '55');
INSERT INTO complaint(status, date, text, type, user_id)
VALUES ('', to_date('2017-03-25', 'YYYY-MM-DD'), 'Complaint3', 'type3', '60');


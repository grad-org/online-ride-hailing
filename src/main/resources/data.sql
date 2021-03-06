INSERT INTO AUTHORITY (ID, NAME) VALUES (1, 'ROLE_PASSENGER');
INSERT INTO AUTHORITY (ID, NAME) VALUES (2, 'ROLE_DRIVER');
INSERT INTO AUTHORITY (ID, NAME) VALUES (3, 'ROLE_ADMIN');

INSERT INTO USER (ID, USERNAME, PASSWORD, NICKNAME, GENDER, AGE, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (1, 'admin', '$2a$10$pZMr1LqJbt71h6X.vnhfheHtuOL0h2yci2Z7Kq/F9p84TkU9b2nRO', '系统管理员', '男', 23, true, '2018-04-06 12:38:08');
INSERT INTO USER (ID, USERNAME, PASSWORD, NICKNAME, GENDER, AGE, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (2, '13413633900', '$2a$10$hWcn0SfrDq24C9es5/jCWObNy937l7tHBYNGdjAeJ1sN2CXRrmCG6', '乘客1', '男', 40, true, '2018-04-06 12:39:41');
INSERT INTO USER (ID, USERNAME, PASSWORD, NICKNAME, GENDER, AGE, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (3, '13413633901', '$2a$10$hWcn0SfrDq24C9es5/jCWObNy937l7tHBYNGdjAeJ1sN2CXRrmCG6', '乘客2', '女', 22, true, '2018-04-06 12:39:54');
INSERT INTO USER (ID, USERNAME, PASSWORD, NICKNAME, GENDER, AGE, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (4, '13413633910', '$2a$10$hWcn0SfrDq24C9es5/jCWObNy937l7tHBYNGdjAeJ1sN2CXRrmCG6', '车主1', '女', 36, true, '2018-04-06 12:40:13');
INSERT INTO USER (ID, USERNAME, PASSWORD, NICKNAME, GENDER, AGE, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (5, '13413633911', '$2a$10$hWcn0SfrDq24C9es5/jCWObNy937l7tHBYNGdjAeJ1sN2CXRrmCG6', '车主2', '男', 25, true, '2018-04-06 12:40:27');
INSERT INTO USER (ID, USERNAME, PASSWORD, NICKNAME, GENDER, AGE, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (6, '13432862055', '$2a$10$hWcn0SfrDq24C9es5/jCWObNy937l7tHBYNGdjAeJ1sN2CXRrmCG6', '越前君', '男', 23, true, '2018-04-16 13:35:30');
INSERT INTO USER (ID, USERNAME, PASSWORD, NICKNAME, GENDER, AGE, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (7, '13432862054', '$2a$10$hWcn0SfrDq24C9es5/jCWObNy937l7tHBYNGdjAeJ1sN2CXRrmCG6', 'Frankie', '男', 23, true, '2018-04-16 13:35:35');

INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 3);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (2, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (3, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (4, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (4, 2);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (5, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (5, 2);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (6, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (7, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (7, 2);

INSERT INTO PASSENGER (ID, USER_ID) VALUES (1, 2);
INSERT INTO PASSENGER (ID, USER_ID) VALUES (2, 3);
INSERT INTO PASSENGER (ID, USER_ID) VALUES (3, 4);
INSERT INTO PASSENGER (ID, USER_ID) VALUES (4, 5);
INSERT INTO PASSENGER (ID, USER_ID) VALUES (5, 6);
INSERT INTO PASSENGER (ID, USER_ID) VALUES (6, 7);

INSERT INTO DRIVER (ID, DRIVER_STATUS, USER_ID) VALUES (1, 'APPROVED', 4);
INSERT INTO DRIVER (ID, DRIVER_STATUS, USER_ID) VALUES (2, 'APPROVED', 5);
INSERT INTO DRIVER (ID, DRIVER_STATUS, USER_ID) VALUES (3, 'APPROVED', 7);

INSERT INTO DRIVING_LICENSE (ID, DRIVER_NAME, IDENTIFICATION, ISSUE_DATE, DRIVER_ID) VALUES (1, '钟富澎', '440681199510113632', '2016-08-08 00:00:00', 1);
INSERT INTO DRIVING_LICENSE (ID, DRIVER_NAME, IDENTIFICATION, ISSUE_DATE, DRIVER_ID) VALUES (2, '钟富浩', '440681198911123632', '2014-08-08 00:00:00', 2);
INSERT INTO DRIVING_LICENSE (ID, DRIVER_NAME, IDENTIFICATION, ISSUE_DATE, DRIVER_ID) VALUES (3, '黎文斌', '441881199602252818', '2016-08-08 00:00:00', 3);

INSERT INTO VEHICLE_LICENSE (ID, OWNER, REGISTER_DATE, DRIVER_ID) VALUES (1, '钟富澎', '2016-08-10 12:00:00', 1);
INSERT INTO VEHICLE_LICENSE (ID, OWNER, REGISTER_DATE, DRIVER_ID) VALUES (2, '钟富浩', '2016-08-10 12:00:00', 2);
INSERT INTO VEHICLE_LICENSE (ID, OWNER, REGISTER_DATE, DRIVER_ID) VALUES (3, '黎文斌', '2016-08-10 12:00:00', 3);

INSERT INTO CAR (ID, PLATE_NO, BRAND, SERIES, COLOR, VEHICLE_LICENSE_ID) VALUES (1, '粤XFP123', '丰田', '凌志', '银色', 1);
INSERT INTO CAR (ID, PLATE_NO, BRAND, SERIES, COLOR, VEHICLE_LICENSE_ID) VALUES (2, '粤XFH123', '丰田', '凌志', '黑色', 2);
INSERT INTO CAR (ID, PLATE_NO, BRAND, SERIES, COLOR, VEHICLE_LICENSE_ID) VALUES (3, '粤A12345', '宝马', 'S级', '白色', 3);

INSERT INTO FARE_RULE (ID, INITIAL_PRICE, INITIAL_MILEAGE, UNIT_PRICE_PER_KILOMETER, UNIT_PRICE_PER_MINUTE, SETUP_TIME) values (1, 10, 2.5, 2.5, 0.5, '2018-04-21 00:00:00');

INSERT INTO DRIVER_BALANCE (ID, ALIPAY_ACCOUNT, BALANCE, DRIVER_ID) VALUES (1, 'dcbgxw6262@sandbox.com', 1000.00, 1);
INSERT INTO DRIVER_BALANCE (ID, ALIPAY_ACCOUNT, BALANCE, DRIVER_ID) VALUES (2, 'dcbgxw6262@sandbox.com', 1000.00, 2);
INSERT INTO DRIVER_BALANCE (ID, ALIPAY_ACCOUNT, BALANCE, DRIVER_ID) VALUES (3, 'dcbgxw6262@sandbox.com', 1000.00, 3);
INSERT INTO AUTHORITY (ID, NAME) VALUES (1, 'ROLE_PASSENGER');
INSERT INTO AUTHORITY (ID, NAME) VALUES (2, 'ROLE_DRIVER');
INSERT INTO AUTHORITY (ID, NAME) VALUES (3, 'ROLE_ADMIN');

INSERT INTO USER (ID, USERNAME, PASSWORD, NICKNAME, GENDER, AGE, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (1, 'admin', '$2a$10$pZMr1LqJbt71h6X.vnhfheHtuOL0h2yci2Z7Kq/F9p84TkU9b2nRO', '系统管理员', '男', 23, true, '2018-04-06 12:38:08');
INSERT INTO USER (ID, USERNAME, PASSWORD, NICKNAME, GENDER, AGE, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (2, 'p1', '$2a$10$lZSKqEQieuQDmcCZXt4dcu1BmMhenvsazrBKyh3wH1F7pBeJY1vRK', '乘客1', '男', 40, true, '2018-04-06 12:39:41');
INSERT INTO USER (ID, USERNAME, PASSWORD, NICKNAME, GENDER, AGE, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (3, 'p2', '$2a$10$Tkg0/d70LJGOKx9xGwF0yOxTQEIBbkR01sowNYlAQLUOJyUVNY5cu', '乘客2', '女', 22, true, '2018-04-06 12:39:54');
INSERT INTO USER (ID, USERNAME, PASSWORD, NICKNAME, GENDER, AGE, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (4, 'd1', '$2a$10$UhZxT4Tf8d3JZ65wdXfDouq5dsrlFoGmrKakJjywq8zQ91alsOCSa', '车主1', '女', 36, true, '2018-04-06 12:40:13');
INSERT INTO USER (ID, USERNAME, PASSWORD, NICKNAME, GENDER, AGE, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (5, 'd2', '$2a$10$gn55bZi9Cb/GrzNvzc87MeK89kQH/0SY3.8hQ901a9jbuWzP.vKXW', '车主2', '男', 25, true, '2018-04-06 12:40:27');
INSERT INTO USER (ID, USERNAME, PASSWORD, NICKNAME, GENDER, AGE, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (6, '13432862055', '$2a$10$hWcn0SfrDq24C9es5/jCWObNy937l7tHBYNGdjAeJ1sN2CXRrmCG6', '越前君', '男', 23, true, '2018-04-16 13:35:30');
INSERT INTO USER (ID, USERNAME, PASSWORD, NICKNAME, GENDER, AGE, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (7, '13432862054', '$2a$10$hWcn0SfrDq24C9es5/jCWObNy937l7tHBYNGdjAeJ1sN2CXRrmCG6', 'Frankie', '男', 23, true, '2018-04-16 13:35:35');

INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 3);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (2, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (3, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (4, 2);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (5, 2);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (6, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (7, 2);

INSERT INTO PASSENGER (ID, USER_ID) VALUES (1, 2);
INSERT INTO PASSENGER (ID, USER_ID) VALUES (2, 3);
INSERT INTO PASSENGER (ID, USER_ID) VALUES (3, 6);

INSERT INTO DRIVER (ID, USER_ID) VALUES (1, 4);
INSERT INTO DRIVER (ID, USER_ID) VALUES (2, 5);
INSERT INTO DRIVER (ID, USER_ID) VALUES (3, 7);

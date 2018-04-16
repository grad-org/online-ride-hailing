DROP TABLE IF EXISTS AUTHORITY;
DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS USER_AUTHORITY;
DROP TABLE IF EXISTS PASSENGER;
DROP TABLE IF EXISTS DRIVER;
DROP TABLE IF EXISTS TRIP;

CREATE TABLE AUTHORITY (
  ID BIGINT NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(255),
  PRIMARY KEY (ID)
);

CREATE TABLE USER (
  ID BIGINT NOT NULL AUTO_INCREMENT,
  USERNAME VARCHAR(255),
  PASSWORD VARCHAR(255),
  NICKNAME VARCHAR(255),
  GENDER VARCHAR(255),
  AGE INT,
  ENABLED INT,
  LAST_PASSWORD_RESET_DATE DATETIME,
  PRIMARY KEY (ID)
);

CREATE TABLE USER_AUTHORITY (
  USER_ID BIGINT,
  AUTHORITY_ID BIGINT
);

CREATE TABLE PASSENGER (
  ID BIGINT NOT NULL AUTO_INCREMENT,
  USER_ID BIGINT,
  PRIMARY KEY (ID)
);

CREATE TABLE DRIVER (
  ID BIGINT NOT NULL AUTO_INCREMENT,
  USER_ID BIGINT,
  PRIMARY KEY (ID)
);

CREATE TABLE TRIP (
  ID BIGINT NOT NULL AUTO_INCREMENT,
  DEPARTURE VARCHAR(255),
  DESTINATION VARCHAR(255),
  TRIP_STATUS VARCHAR(255),
  CREATED_TIME DATETIME,
  DEPARTURE_TIME DATETIME,
  PASSENGER_ID BIGINT,
  PRIMARY KEY (ID)
);

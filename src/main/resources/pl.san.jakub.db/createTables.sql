-- Creates tables for reservation system DB
-- Author: Jakub Pustelnik

BEGIN;

CREATE TABLE Credentials (
    IP varchar(13) PRIMARY KEY,
    login varchar(20) NOT NULL,
    password varchar(20) NOT NULL
);


CREATE TABLE Servers (
    HOST_NAME varchar(12) PRIMARY KEY,
    OS_IP varchar(13) REFERENCES Credentials(IP),
    IRMC_IP varchar(13) REFERENCES Credentials(IP)
);
CREATE SEQUENCE Profile_id_seq START 1;

CREATE TABLE Profile(
   ID int PRIMARY KEY,
   firstName varchar(20) NOT NULL,
   lastName varchar(20) NOT NULL,
   login varchar(20) NOT NULL,
   password varchar(20) NOT NULL,
   email varchar(20) NOT NULL,
   security_level varchar(5) NOT NULL,
   host_name varchar(12) REFERENCES Servers(HOST_NAME)
);
END;
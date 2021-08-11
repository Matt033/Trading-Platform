CREATE DATABASE IF NOT EXISTS assignment4;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Favorites;
DROP TABLE IF EXISTS Portfolio;
CREATE TABLE assignment4.Users(
	ID INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255),
    balance DOUBLE,
    PRIMARY KEY (ID)
);

CREATE TABLE assignment4.Favorites(
	username VARCHAR(255),
    ticker VARCHAR(255)
);

CREATE TABLE assignment4.Portfolio(
	ID INT NOT NULL AUTO_INCREMENT,
	username VARCHAR(255),
    ticker VARCHAR(255),
    askPrice DOUBLE,
    quantity INT,
    total DOUBLE,
    PRIMARY KEY (ID)
);




create database if not exists cardideadb;

use cardideadb;

create table if not exists CARDS(
    id int NOT NULL AUTO_INCREMENT,
    cardset varchar(45) NOT NULL,
    topic varchar(45),
    question varchar(90) NOT NULL,
    solution varchar(180),
    hint varchar(180),
    PRIMARY KEY (id)
);

create user if not exists 'dbadmin'@'localhost' identified by 'password';
grant all privileges on CARDS to 'dbadmin'@'localhost' identified by 'password';
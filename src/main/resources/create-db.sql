create database if not exists cardideadb;

use cardideadb;

create table if not exists CARDSET(
    id integer NOT NULL AUTO_INCREMENT,
    name varchar(45),
    PRIMARY KEY (id)
);

create table if not exists CARD(
    id integer NOT NULL AUTO_INCREMENT,
    cardset_id integer NOT NULL,
    topic varchar(45),
    question varchar(90) NOT NULL,
    solution varchar(180),
    hint varchar(180),
    PRIMARY KEY (id)
    FOREIGN KEY (cardset_id) REFERENCES CARD_SETS(id)
);

create user if not exists 'dbadmin'@'localhost' identified by 'password';
grant all privileges on cardideadb to 'dbadmin'@'localhost' identified by 'password';
create table CHATROOMS(
    ID INTEGER not null AUTO_INCREMENT,
    NAME varchar(255) NOT NULL,
    ACTIVE boolean NOT NULL,
    PRIMARY KEY (ID),
    UNIQUE (NAME)
);
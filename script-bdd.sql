DROP DATABASE IF EXISTS paymybuddydb;
CREATE DATABASE paymybuddydb;
USE paymybuddydb;

CREATE TABLE bank_account (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              account_number VARCHAR(255),
                              balance NUMERIC(38,2)
);

CREATE TABLE my_user (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255),
                         username VARCHAR(255),
                         bank_account_id INT UNIQUE,
                         CONSTRAINT fk_my_user_bank_account FOREIGN KEY (bank_account_id) REFERENCES bank_account(id)
);

CREATE TABLE connections (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             user_id INT,
                             targeted_user_id INT,
                             CONSTRAINT fk_connections_user FOREIGN KEY (user_id) REFERENCES my_user(id),
                             CONSTRAINT fk_connections_target FOREIGN KEY (targeted_user_id) REFERENCES my_user(id),
                             CONSTRAINT unique_connections UNIQUE (user_id, targeted_user_id)
);

CREATE TABLE my_user_connections (
                                     user_id INT NOT NULL,
                                     connections_id INT NOT NULL UNIQUE,
                                     CONSTRAINT fk_my_user_connections_user FOREIGN KEY (user_id) REFERENCES my_user(id),
                                     CONSTRAINT fk_my_user_connections_conn FOREIGN KEY (connections_id) REFERENCES connections(id)
);

CREATE TABLE transaction (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             amount NUMERIC(38,2),
                             beneficiary_username VARCHAR(255),
                             description VARCHAR(255),
                             bank_account_id INT,
                             CONSTRAINT fk_transaction_bank_account FOREIGN KEY (bank_account_id) REFERENCES bank_account(id)
);







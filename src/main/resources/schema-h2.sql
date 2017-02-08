-- dropping tables
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS books;
-- users table
CREATE TABLE users (id INT NOT NULL AUTO_INCREMENT primary key, name varchar(50) NOT NULL UNIQUE, password varchar(255), enabled bit DEFAULT 1);
-- authorities table
CREATE TABLE authorities (username VARCHAR(50) NOT NULL, authority VARCHAR(50) NOT NULL DEFAULT 'user', CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users(name) ON DELETE CASCADE);
-- books table
CREATE TABLE books (id INT NOT NULL AUTO_INCREMENT primary key, isbn varchar(17) NOT NULL, author varchar(50) NOT NULL, name varchar(50) NOT NULL, takerid int REFERENCES users(id) ON DELETE SET NULL);

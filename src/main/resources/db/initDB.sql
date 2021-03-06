DROP SCHEMA PUBLIC CASCADE;

-- DROP TABLE dishes IF EXISTS;
-- DROP TABLE restaurants IF EXISTS;
-- DROP TABLE user_roles IF EXISTS;
-- DROP TABLE voting IF EXISTS;
-- DROP TABLE users IF EXISTS;
--
--
-- DROP SEQUENCE USER_SEQ IF EXISTS;
-- DROP SEQUENCE REST_SEQ IF EXISTS;
-- DROP SEQUENCE DISH_SEQ IF EXISTS;
-- DROP SEQUENCE VOTE_SEQ IF EXISTS;

CREATE SEQUENCE USER_SEQ AS INTEGER START WITH 1000 INCREMENT BY 1;

CREATE SEQUENCE REST_SEQ AS INTEGER START WITH 1000 INCREMENT BY 1;

CREATE SEQUENCE DISH_SEQ AS INTEGER START WITH 1000 INCREMENT BY 1;

CREATE SEQUENCE VOTE_SEQ AS INTEGER START WITH 1000 INCREMENT BY 1;

CREATE TABLE users
(
  id         INTEGER GENERATED BY DEFAULT AS SEQUENCE USER_SEQ PRIMARY KEY,
  name       VARCHAR(255)            NOT NULL,
  email      VARCHAR(255)            NOT NULL,
  password   VARCHAR(255)            NOT NULL,
  registered TIMESTAMP DEFAULT now() NOT NULL

);

CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE restaurants
(
  id   INTEGER GENERATED BY DEFAULT AS SEQUENCE REST_SEQ PRIMARY KEY,
  name VARCHAR(255) UNIQUE
);

CREATE TABLE dishes
(
  id            INTEGER GENERATED BY DEFAULT AS SEQUENCE DISH_SEQ PRIMARY KEY,
  name          VARCHAR(255) UNIQUE     NOT NULL,
  restaurant_id INTEGER                 NOT NULL,
  price         VARCHAR(255)            NOT NULL,
  added         TIMESTAMP DEFAULT now() NOT NULL,
  FOREIGN KEY (restaurant_id) REFERENCES RESTAURANTS (id) ON DELETE CASCADE
);


CREATE TABLE voting
(
  id            INTEGER GENERATED BY DEFAULT AS SEQUENCE VOTE_SEQ PRIMARY KEY,
  user_id       INTEGER                 NOT NULL,
  restaurant_id INTEGER                 NOT NULL,
  date          TIMESTAMP DEFAULT now() NOT NULL,
  FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE,
  FOREIGN KEY (restaurant_id) REFERENCES RESTAURANTS (id) ON DELETE CASCADE
);

CREATE TABLE USER_ROLES
(
  user_id INTEGER NOT NULL,
  role    VARCHAR(255),
  CONSTRAINT user_roles_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);
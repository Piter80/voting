DELETE FROM USER_ROLES;
DELETE FROM RESTAURANTS;
DELETE FROM DISHES;
DELETE FROM VOTING;
DELETE FROM USERS;

ALTER SEQUENCE USER_SEQ RESTART WITH 1000;
ALTER SEQUENCE REST_SEQ RESTART WITH 1000;
ALTER SEQUENCE DISH_SEQ RESTART WITH 1000;
ALTER SEQUENCE VOTE_SEQ RESTART WITH 1000;

INSERT INTO USERS (NAME, EMAIL, PASSWORD, REGISTERED)
VALUES ('User', 'user@ya.ru', '{noop}pass', '2019-05-30 10:00:00'),
       ('Admin', 'admin@ya.ru', '{noop}pass', '2019-05-30 10:00:00');

INSERT INTO USER_ROLES (ROLE, USER_ID)
VALUES ('ROLE_USER', 1000),
       ('ROLE_ADMIN', 1001),
       ('ROLE_USER', 1001);

INSERT INTO RESTAURANTS (NAME)
VALUES ('Breaking eat'),
       ('PigKing');

INSERT INTO DISHES (NAME, PRICE, RESTAURANT_ID, ADDED)
VALUES ('Eat and break', '25.39', 1000, '2019-05-30 10:00:00'),
       ('Go-go in toilet', '45.51', 1000, '2019-05-30 10:00:00'),
       ('Omnomnom', '60.99', 1000, '2019-05-30 10:00:00'),
       ('PigInSoil', '24.39', 1001, '2019-05-30 10:00:00'),
       ('PiggerSwin', '44.51', 1001, '2019-05-30 10:00:00'),
       ('Fat Pa', '59.99', 1001, '2019-05-30 10:00:00');

INSERT INTO VOTING (USER_ID, RESTAURANT_ID, DATE)
VALUES (1001, 1000, '2019-05-30 10:00:00'),
       (1000, 1001, '2019-05-30 10:00:00');
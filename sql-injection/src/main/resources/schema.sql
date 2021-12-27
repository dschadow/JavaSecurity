CREATE TABLE customers
(
    id          INTEGER     NOT NULL,
    name        VARCHAR(50) NOT NULL,
    status      VARCHAR(50),
    order_limit INTEGER     NOT NULL,
    PRIMARY KEY (id)
);
DROP SCHEMA IF EXISTS accounts CASCADE;

CREATE SCHEMA IF NOT EXISTS accounts;
SET schema 'accounts';

CREATE TABLE accounts
(
    id              BIGSERIAL PRIMARY KEY,
    account_number  VARCHAR(16) UNIQUE  NOT NULL,
    account_balance NUMERIC(15, 2) NOT NULL CHECK (account_balance >= 0),
    created_at      TIMESTAMP      NOT NULL DEFAULT NOW(),
    user_id         BIGINT,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users.users (id) ON DELETE SET NULL
);

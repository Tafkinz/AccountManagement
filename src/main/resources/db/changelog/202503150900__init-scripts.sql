--liquibase formatted sql

--changeset tafkin:1
CREATE SEQUENCE IF NOT EXISTS ACCOUNT_SEQ START WITH 1000 INCREMENT BY 3;

create table account
(
    id            BIGINT PRIMARY KEY,
    name          VARCHAR(255)             NOT NULL,
    phone_nr       VARCHAR(64) NULL UNIQUE,
    created_dtime  TIMESTAMP WITH TIME ZONE,
    modified_dtime TIMESTAMP WITH TIME ZONE
);

create unique index idx_account_phone_nr on account (phone_nr);
create index idx_account_name on account (name);

CREATE TRIGGER trg_account_update_audit BEFORE UPDATE ON account
    FOR EACH ROW
    CALL "ee.tafkin.config.AuditTrigger";

CREATE TRIGGER trg_account_insert_audit BEFORE INSERT ON account
    FOR EACH ROW
    CALL "ee.tafkin.config.AuditTrigger";

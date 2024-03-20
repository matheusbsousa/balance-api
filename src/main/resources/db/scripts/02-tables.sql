-- Category --------------------------------------------------------------------------------------------------------
create sequence balance.category_id_seq;

create table balance.category
(
    id     BIGINT PRIMARY KEY DEFAULT nextval('balance.category_id_seq'),
    name   varchar(100),
    values VARCHAR(500)
);
------------------------------------------------------------------------------------------------------------------

-- Entry ---------------------------------------------------------------------------------------------------------
create sequence balance.entry_id_seq;

create table balance.entry
(
    id                   BIGINT PRIMARY KEY        DEFAULT nextval('balance.entry_id_seq'),
    is_ignored           boolean          not null default false,
    value                double precision not null,
    category_id          bigint
        constraint fk_category_id
            references balance.category,
    date                 timestamp(6),
    description          varchar(100),
    original_description varchar(100)
);
--------------------------------------------------------------------------------------------------------------------

-- Month Limit --------------------------------------------------------------------------------------------------------
create sequence balance.month_limit_id_seq;

create table balance.month_limit
(
    month             integer not null,
    year              integer not null,
    id                BIGINT PRIMARY KEY DEFAULT nextval('balance.month_limit_id_seq'),

    month_description varchar(25)
);
------------------------------------------------------------------------------------------------------------------

-- Limit --------------------------------------------------------------------------------------------------------
create sequence balance.limit_id_seq;

create table balance.limit
(
    percentage     double precision not null,
    id             BIGINT PRIMARY KEY DEFAULT nextval('balance.limit_id_seq'),
    month_limit_id bigint
        constraint fk_month_limit_id
            references balance.month_limit,
    description    varchar(100)
);
------------------------------------------------------------------------------------------------------------------
-- Category Limit --------------------------------------------------------------------------------------------------------
CREATE TABLE balance.category_limit
(
    category_id BIGINT REFERENCES balance.category(id),
    limit_id BIGINT REFERENCES balance.limit(id),
    PRIMARY KEY (category_id, limit_id)
);
------------------------------------------------------------------------------------------------------------------

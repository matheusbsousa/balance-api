-- Limit Category ------------------------------------------------------------------------------------------------
create sequence balance.limit_category_id_seq;

create table balance.limit_category
(
    id     BIGINT PRIMARY KEY DEFAULT nextval('balance.limit_category_id_seq'),
    "limit"     integer not null
        constraint limit_category_limit_fk
            references balance."limit",
    category    integer not null
        constraint limit_category_category_fk
            references balance.category,
    limit_value integer
);
------------------------------------------------------------------------------------------------------------------

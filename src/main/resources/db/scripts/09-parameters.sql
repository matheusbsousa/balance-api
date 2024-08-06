create sequence balance.parameters_id_seq;

create table balance.parameters
(
    id                     bigint primary key    default nextval('parameters_id_seq'),
    first_day_of_the_month numeric      not null default 1,
    unknown_category_name    varchar(100) not null default 'Unknown'
)
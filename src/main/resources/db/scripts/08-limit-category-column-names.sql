alter table balance.limit_category
    rename column "limit" to limit_id;

alter table balance.limit_category
    rename column category to category_id;


alter table balance.entry
    alter column value set not null;

alter table balance.entry
    alter column date set not null;

alter table balance.entry
    alter column description set not null;

alter table balance.entry
    alter column original_description set not null;

alter table balance.entry
    alter column original_value set not null;

alter table balance.entry
    alter column original_date set not null;

alter table balance.entry
    add month integer;

alter table balance.entry
    add type varchar default 'EXPENSE';


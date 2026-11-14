alter table app_user add column failed_attempts int not null default 0;
alter table app_user add column locked boolean not null default false;

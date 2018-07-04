alter table practice_log modify body json;
alter table practice_log change practice_date created_at datetime not null;

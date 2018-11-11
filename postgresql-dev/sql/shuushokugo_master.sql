create table shuushokugo_master (
	shuushokugocode integer not null primary key,
	name varchar(40) not null check (char_length(name) > 0)
);
create table kizai_master (
	kizaicode int not null check (kizaicode > 0),
	name varchar(32) not null check(char_length(name) > 0),
	yomi varchar(20) not null check(char_length(yomi) > 0),
	unit varchar(6) not null,
	kingaku decimal(11, 2) not null,
	valid_from date not null,
	valid_upto date,
	check(valid_upto is null or (valid_from <= valid_upto)),
	primary key(kizaicode, valid_from)
);

create or replace function check_kizai_master_fun() returns trigger as $$
	declare
		count integer;
	begin
		select count(*) into count from public.kizai_master 
			where kizaicode = new.kizaicode 
			and valid_from <> new.valid_from
			and not (
				(valid_upto is not null and valid_upto < new.valid_from) or
				(new.valid_upto is not null and new.valid_upto < valid_from)
			);
		if count > 0 then
			raise exception 'conflicting kizai_master row exists';
		end if;
		return new;
	end;
$$ language plpgsql;

create trigger check_kizai_master before insert or update on kizai_master
	for each row execute procedure check_kizai_master_fun();
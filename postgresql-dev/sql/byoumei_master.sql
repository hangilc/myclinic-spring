create table byoumei_master (
	shoubyoumeicode integer not null check (shoubyoumeicode > 0),
	name varchar(60) not null check (char_length(name) > 0),
	valid_from date not null,
	valid_upto date,
	primary key (shoubyoumeicode, valid_from)
);

create or replace function check_byoumei_master_fun() returns trigger as $$
	declare
		count integer;
	begin
		select count(*) into count from public.byoumei_master where shoubyoumeicode = new.shoubyoumeicode 
			and valid_from <> new.valid_from
			and not (
				(valid_upto is not null and valid_upto < new.valid_from) or
				(new.valid_upto is not null and new.valid_upto < valid_from)
			);
		if count > 0 then
			raise exception 'conflicting byoumei_master row exists';
		end if;
		return new;
	end;
$$ language plpgsql;

create trigger check_byoumei_master before insert or update on byoumei_master
	for each row execute procedure check_byoumei_master_fun();
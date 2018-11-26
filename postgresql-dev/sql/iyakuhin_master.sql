create table iyakuhin_master (
	iyakuhincode int not null check (iyakuhincode > 0),
	name varchar(32) not null check(char_length(name) > 0),
	yomi varchar(20) not null check(char_length(yomi) > 0),
	unit varchar(6) not null,
	yakka decimal(9, 2) not null check(yakka >= 0),
	madoku char(1) not null check(madoku in ('0', '1', '2', '3', '5')),
	kouhatsu char(1) not null check(kouhatsu in ('0', '1')),
	zaikei char(1) not null check(zaikei in ('1' ,'3' ,'4' ,'6' ,'8')),
	valid_from date not null,
	valid_upto date,
	check(valid_upto is null or (valid_from <= valid_upto)),
	primary key(iyakuhincode, valid_from)
);

create or replace function check_iyakuhin_master_fun() returns trigger as $$
	declare
		count integer;
	begin
		select count(*) into count from public.iyakuhin_master 
			where iyakuhincode = new.iyakuhincode 
			and valid_from <> new.valid_from
			and not (
				(valid_upto is not null and valid_upto < new.valid_from) or
				(new.valid_upto is not null and new.valid_upto < valid_from)
			);
		if count > 0 then
			raise exception 'conflicting iyakuhin_master row exists';
		end if;
		return new;
	end;
$$ language plpgsql;

create trigger check_iyakuhin_master before insert or update on iyakuhin_master
	for each row execute procedure check_iyakuhin_master_fun();
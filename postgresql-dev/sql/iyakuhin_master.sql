create table iyakuhin_master (
	iyakuhincode int not null check (iyakuhincode > 0),
	name varchar(64) not null check(char_length(name) > 0),
	yomi varchar(32) not null check(char_length(yomi) > 0),
	unit varchar(16) not null check(char_length(unit) > 0),
	yakka decimal(10, 2) not null check(yakka >= 0),
	madoku char(1) not null check(madoku = '0' or madoku = '1' or madoku = '2' or madoku = '3' or madoku = '5'),
	kouhatsu char(1) not null check(kouhatsu = '0' or kouhatsu = '1'),
	zaikei char(1) not null check(zaikei = '1' or zaikei = '3' or zaikei = '4' or zaikei = '6' or zaikei = '8'),
	valid_from date not null,
	valid_upto date,
	check(valid_upto is null or (valid_from <= valid_upto)),
	primary key(iyakuhincode, valid_from)
);

create or replace function check_iyakuhin_master_fun() returns trigger as $$
	declare
		count integer;
	begin
		select * into count from iyakuhin_master where iyakuhincode = new.iyakuhincode 
			and valid_from <> new.valid_from
			and not (
				(valid_upto is not null and valid_upto < new.valid_from) or
				(new.valid_upto is not null and new.valid_upto < valid_from)
			);
		if count > 0 then
			raise exception 'conflicing iyakuhin_master row exists';
		end if;
		return new;
	end;
$$ language plpgsql;

create trigger check_iyakuhin_master before insert or update on iyakuhin_master
	for each row execute procedure check_iyakuhin_master_fun();
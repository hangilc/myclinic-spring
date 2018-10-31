create table shinryou_master (
	shinryoucode int not null check (shinryoucode > 0),
	name varchar(32) not null check(char_length(name) > 0),
	tensuu decimal(9, 2) not null,
	tensuu_shikibetsu char(1) not null check(tensuu_shikibetsu in ('1' ,'3' ,'4','5' ,'6' ,'7','8')),
	shuukeisaki varchar(3) not null check(shuukeisaki in ('0',
		'110', '120', '122', '123', '124', '125', '130', '140', '210', '230', '240',
		'250', '260', '270', '300', '311', '321', '331', '400', '500', '502', '540',
		'600', '700', '800', '903', '920', '970', '971', '972', '973', '974', '975')),  
	houkatsukensa char(2) not null check(houkatsukensa in (
		'00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12')),
	kensagroup char(2) not null check(kensagroup in (
	    '00', '01', '02', '03', '04', '05', '06', '07', '08', '11', '12', '13',
	    '14', '15', '16', '31', '32', '33', '40', '41', '42'
	)),
	valid_from date not null,
	valid_upto date,
	check(valid_upto is null or (valid_from <= valid_upto)),
	primary key(shinryoucode, valid_from)
);

create or replace function check_shinryou_master_fun() returns trigger as $$
	declare
		count integer;
	begin
		select count(*) into count from shinryou_master where shinryoucode = new.shinryoucode 
			and valid_from <> new.valid_from
			and not (
				(valid_upto is not null and valid_upto < new.valid_from) or
				(new.valid_upto is not null and new.valid_upto < valid_from)
			);
		if count > 0 then
			raise exception 'conflicting shinryou_master row exists';
		end if;
		return new;
	end;
$$ language plpgsql;

create trigger check_shinryou_master before insert or update on shinryou_master
	for each row execute procedure check_shinryou_master_fun();
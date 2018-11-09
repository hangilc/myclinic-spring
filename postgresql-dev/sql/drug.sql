create table drug (
	drug_id integer generated by default as identity primary key,
	visit_id integer not null references visit (visit_id),
	iyakuhincode integer not null check (iyakuhincode > 0),
	amount decimal(6,2) not null check (amount >= 0),
	usage varchar(255) not null,
	days smallint not null check (days >= 0),
	category smallint not null check (category in (0, 1, 2, 3)),
	prescribed smallint not null check (prescribed in (0, 1))
);

create or replace function check_drug_fun() returns trigger as $$
	declare
		count integer;
	begin
		select count(*) into count from iyakuhin_master m, visit v where m.iyakuhincode = new.iyakuhincode
			and v.visit_id = new.visit_id 
			and m.valid_from <= date(v.visited_at)
			and (m.valid_upto is null or m.valid_upto >= date(v.visted_at));
		if count < 1 then
			raise exception "cannot find iyakuhin master for drug";
		end if;
		if count > 1 then
			raise exception 'found multiple iyakuhin masters for drug';
		end if;
		return new;
	end;
$$ language plpgsql;

create trigger check_drug before insert or update on drug
	for each row execute procedure check_drug_fun();
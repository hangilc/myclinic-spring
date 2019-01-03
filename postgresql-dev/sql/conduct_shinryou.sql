create table conduct_shinryou (
	conduct_shinryou_id integer generated by default as identity primary key,
	conduct_id integer not null references conduct (conduct_id),
	shinryoucode integer not null check (shinryoucode > 0)
);

create or replace function check_conduct_shinryou_fun() returns trigger as $$
	declare
		count integer;
	begin
		select count(*) into count from public.shinryou_master m, public.conduct c, public.visit v 
			where m.shinryoucode = new.shinryoucode
			and new.conduct_id = c.conduct_id
			and v.visit_id = c.visit_id 
			and m.valid_from <= date(v.visited_at)
			and (m.valid_upto is null or m.valid_upto >= date(v.visited_at));
		if count < 1 then
			raise exception 'cannot find shinryou master for conduct shinryou';
		end if;
		if count > 1 then
			raise exception 'found multiple shinryou masters for conduct shinryou';
		end if;
		return new;
	end;
$$ language plpgsql;

create trigger check_conduct_shinryou before insert or update on conduct_shinryou
	for each row execute procedure check_conduct_shinryou_fun();
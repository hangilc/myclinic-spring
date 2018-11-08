create table wqueue (
	visit_id integer not null references visit (visit_id) primary key,
	wait_state smallint not null check(wait_state in (0, 1, 2, 3, 4))
);
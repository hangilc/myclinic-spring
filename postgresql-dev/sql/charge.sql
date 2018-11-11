create table charge (
	visit_id integer not null references visit (visit_id) primary key,
	charge smallint not null check (charge >= 0)
);
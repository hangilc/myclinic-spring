create table payment (
	visit_id integer not null references visit (visit_id),
	amount smallint not null check (amount >= 0),
	paytime timestamp not null,
	primary key (visit_id, paytime)
);
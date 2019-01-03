create table pharma_queue (
	visit_id integer not null references visit (visit_id) primary key,
	pharma_state smallint not null check(pharma_state in (0, 1, 2))
);
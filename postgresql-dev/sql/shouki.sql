create table shouki (
	visit_id integer not null references visit (visit_id) primary key,
	shouki text not null
);
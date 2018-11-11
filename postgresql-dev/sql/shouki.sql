create table shouki (
	visit_id integer not null references visit (visit_id),
	shouki text not null
);
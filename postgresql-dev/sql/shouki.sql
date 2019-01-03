create table shouki (
	visit_id integer not null references visit (visit_id) on delete cascade primary key,
	shouki text not null
);
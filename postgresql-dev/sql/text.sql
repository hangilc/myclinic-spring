create table text (
	text_id integer generated by default as identity primary key,
	visit_id integer not null references visit (visit_id),
	content text not null
);

create index on text (visit_id);

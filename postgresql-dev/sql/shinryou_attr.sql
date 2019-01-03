create table shinryou_attr (
	shinryou_id integer not null references shinryou (shinryou_id) on delete cascade primary key,
	tekiyou text not null
);
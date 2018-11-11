create table shinryou_attr (
	shinryou_id integer not null references shinryou (shinryou_id),
	tekiyou text not null
);
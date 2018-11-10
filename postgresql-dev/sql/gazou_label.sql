create table gazou_label (
	conduct_id integer not null references conduct (conduct_id),
	label varchar(255)
);
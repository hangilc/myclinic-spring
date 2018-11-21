create table gazou_label (
	conduct_id integer not null references conduct (conduct_id) primary key,
	label varchar(255)
);
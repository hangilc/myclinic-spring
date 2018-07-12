create table shinryou_attr (
	shinryou_id int unsigned not null primary key,
	tekiyou text,
	foreign key fk_attr_shinryou_id (shinryou_id) references visit_shinryou(shinryou_id)
	    on delete cascade
);
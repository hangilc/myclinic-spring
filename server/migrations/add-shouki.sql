create table shouki (
	visit_id int unsigned not null primary key,
	shouki text,
	foreign key fk_shouki_visit_id (visit_id) references visit(visit_id)
	    on delete cascade
);
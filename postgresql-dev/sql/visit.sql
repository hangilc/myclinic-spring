create table visit (
	visit_id integer generated by default as identity primary key,
	patient_id integer not null references patient (patient_id),
	visited_at timestamp not null,
	shahokokuho_id integer references shahokokuho (shahokokuho_id),
	roujin_id integer references roujin (roujin_id),
	koukikourei_id integer references koukikourei (koukikourei_id),
	kouhi_1_id integer references kouhi (kouhi_id),
	kouhi_2_id integer references kouhi (kouhi_id),
	kouhi_3_id integer references kouhi (kouhi_id)
);

create index on visit (patient_id);
create index on visit (visited_at);
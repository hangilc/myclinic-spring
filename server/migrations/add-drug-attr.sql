create table drug_attr (
	drug_id int unsigned not null primary key,
	tekiyou text,
	foreign key fk_attr_drug_id (drug_id) references visit_drug(drug_id)
	    on delete cascade
);
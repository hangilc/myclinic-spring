create table drug_attr (
	drug_id integer not null references drug (drug_id),
	tekiyou text not null
);
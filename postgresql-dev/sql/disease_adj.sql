create table disease_adj (
	disease_adj_id integer generated by default as identity primary key,
	disease_id integer not null references disease (disease_id),
	shuushokugocode integer not null references shuushokugo_master (shuushokugocode)
);
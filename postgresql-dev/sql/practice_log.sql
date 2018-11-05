create table practice_log (
	practice_log_id serial,
	created_at timestamp,
	kind varchar(32),
	body json
);
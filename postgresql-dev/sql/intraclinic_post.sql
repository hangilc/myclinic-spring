create table intraclinic_post (
	post_id integer generated by default as identity primary key,
	content text not null,
	created_at date not null
);
create table intraclinic_tag_post (
	tag_id integer not null references intraclinic_tag (tag_id),
	post_id integer not null references intraclinic_post (post_id),
	primary key (tag_id, post_id)
);
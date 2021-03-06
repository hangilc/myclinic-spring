create table hotline (
	hotline_id integer generated by default as identity primary key,
	message text not null,
	sender varchar(255) not null check (char_length(sender) > 0),
	recipient varchar(255) not null check (char_length(recipient) > 0),
	posted_at timestamp not null
);
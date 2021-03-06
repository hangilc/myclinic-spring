create table patient (
	patient_id integer generated by default as identity primary key,
	last_name varchar(20) not null check (char_length(last_name) > 0),
	first_name varchar(20) not null check (char_length(first_name) > 0),
	last_name_yomi varchar(20) not null check (char_length(last_name_yomi) > 0),
	first_name_yomi varchar(20) not null check (char_length(first_name_yomi) > 0),
	sex char(1) not null check (sex = 'M' or sex = 'F'),
	birthday date not null,
	address varchar(80) not null,
	phone varchar(80) not null
);
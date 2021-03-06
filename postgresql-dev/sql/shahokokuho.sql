create table shahokokuho (
	shahokokuho_id integer generated by default as identity primary key,
	patient_id integer not null references patient (patient_id),
	hokensha_bangou integer not null,
	hihokensha_kigou varchar(255) not null,
	hihokensha_bangou varchar(255) not null,
	honnin smallint not null check(honnin = 0 or honnin = 1),
	valid_from date not null,
	valid_upto date,
	kourei smallint not null check(kourei in (0, 1, 2, 3)),
	check(valid_upto is null or (valid_from <= valid_upto)),
	check(char_length(hihokensha_kigou) > 0 or char_length(hihokensha_bangou) > 0)
);

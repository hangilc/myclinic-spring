create table intraclinic_post as select * from intraclinic.post;
alter table intraclinic_post modify id int not null primary key auto_increment;
create table intraclinic_comment as select * from intraclinic.comment;
alter table intraclinic_comment modify id int not null primary key auto_increment;
create table intraclinic_tag as select * from intraclinic.tag;
alter table intraclinic_tag modify id int not null primary key auto_increment;
alter table intraclinic_tag modify name varchar(255) unique not null;
create table intraclinic_tag_post as select * from intraclinic.tag_post;

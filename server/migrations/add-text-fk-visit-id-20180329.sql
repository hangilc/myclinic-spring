alter table visit_text add constraint fk_visit_id foreign key (visit_id) references visit (visit_id);

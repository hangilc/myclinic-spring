alter table patient modify patient_id int;
alter table disease modify patient_id int;
alter table disease add constraint fk_disease_patient_id foreign key (patient_id) references patient (patient_id);
alter table hoken_koukikourei modify patient_id int;
alter table hoken_koukikourei add constraint fk_hoken_koukikourei_patient_id foreign key (patient_id) references patient (patient_id);
alter table hoken_roujin modify patient_id int;
alter table hoken_roujin add constraint fk_hoken_roujin_patient_id foreign key (patient_id) references patient (patient_id);
alter table hoken_shahokokuho modify patient_id int;
alter table hoken_shahokokuho add constraint fk_hoken_shahokokuho_patient_id foreign key (patient_id) references patient (patient_id);
alter table kouhi modify patient_id int;
alter table kouhi add constraint fk_kouhi_patient_id foreign key (patient_id) references patient (patient_id);

alter table visit drop index patient_id;
alter table visit modify patient_id int;
alter table visit add constraint fk_visit_patient_id foreign key (patient_id) references patient (patient_id);

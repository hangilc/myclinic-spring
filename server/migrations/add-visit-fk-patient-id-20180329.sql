alter table visit add constraint fk_patient_id foreign key (patient_id) references patient (patient_id);

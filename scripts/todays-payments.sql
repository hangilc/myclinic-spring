select p.patient_id, p.last_name, p.first_name, c.charge from visit v, patient p, visit_charge c 
where date(v_datetime) = date(now()) and v.patient_id = p.patient_Id and v.visit_id = c.visit_id;

select sum(c.charge) from visit v, visit_charge c 
where date(v_datetime) = date(now()) and v.visit_id = c.visit_id;

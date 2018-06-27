select d.id, d.iyakuhincode, m.iyakuhincode from (visit_conduct_drug d inner join visit_conduct c on d.visit_conduct_id = c.id )
	inner join visit v on c.visit_id = v.visit_id
	left outer join iyakuhin_master_arch m 
	on d.iyakuhincode = m.iyakuhincode 
	and m.valid_from <= date(v.v_datetime) 
	and (m.valid_upto = '0000-00-00' or m.valid_upto >= date(v.v_datetime))
	where m.iyakuhincode is null
	;

select d.id, count(*) as c from visit_conduct_drug d, visit_conduct c, visit v, iyakuhin_master_arch m
	where d.visit_conduct_id = c.id
	and c.visit_id = v.visit_id and d.iyakuhincode = m.iyakuhincode
	and m.valid_from <= date(v.v_datetime) 
	and (m.valid_upto = '0000-00-00' or m.valid_upto >= date(v.v_datetime))
	group by d.id, d.iyakuhincode
	having c != 1
	;
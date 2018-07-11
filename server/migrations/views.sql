drop view if exists shinryou_view;

create view shinryou_view as select
    s.shinryou_id,
    v.visit_id,
    v.v_datetime visited_at,
    m.name,
    s.shinryoucode
    from visit_shinryou s, visit v, shinryoukoui_master_arch m
    where s.visit_id = v.visit_id and
        m.shinryoucode = s.shinryoucode and
        m.valid_from <= date(v.v_datetime) and
            (m.valid_upto = '0000-00-00' or m.valid_upto >= date(v.v_datetime))
    ;
drop table if exists conduct_drug cascade;
drop table if exists gazou_label cascade;
drop table if exists conduct cascade;
drop table if exists shinryou cascade;
drop table if exists drug cascade;
drop table if exists text cascade;
drop table if exists pharma_queue cascade;
drop table if exists wqueue cascade;
drop table if exists visit cascade;
drop table if exists kouhi cascade;
drop table if exists koukikourei cascade;
drop table if exists roujin cascade;
drop table if exists shahokokuho cascade;
drop table if exists patient cascade;
drop table if exists practice_log cascade;

\i practice_log.sql
\i patient.sql
\i shahokokuho.sql
\i roujin.sql
\i koukikourei.sql
\i kouhi.sql
\i visit.sql
\i wqueue.sql
\i pharma_queue.sql
\i text.sql
\i drug.sql
\i shinryou.sql
\i conduct.sql
\i gazou_label.sql
\i conduct_drug.sql



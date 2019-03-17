create table byoumei_master (
  shoubyoumeicode integer,
  name text,
  valid_from text,
  valid_upto text,
  primary key(shoubyoumeicode, valid_from)
);

create table charge (
  visit_id integer primary key,
  charge integer
);

create table conduct (
  conduct_id integer primary key autoincrement,
  visit_id integer,
  kind integer
);

create table conduct_drug (
  conduct_drug_id integer primary key autoincrement,
  conduct_id integer,
  iyakuhincode integer,
  amount text
);

create table conduct_kizai (
  conduct_kizai_id integer primary key autoincrement,
  conduct_id integer,
  kizaicode integer,
  amount text
);

create table conduct_shinryou (
  conduct_shinryou_id integer primary key autoincrement,
  conduct_id integer,
  shinryoucode integer
);

create table disease (
  disease_id integer primary key autoincrement,
  patient_id integer,
  shoubyoumeicode integer,
  start_date text,
  end_date text,
  end_reason text
);

create table disease_adj (
  disease_adj_id integer primary key autoincrement,
  disease_id integer,
  shuushokugocode integer
);

create table drug (
  drug_id integer primary key autoincrement,
  visit_id integer,
  iyakuhincode integer,
  amount text,
  usage text,
  days integer,
  category integer,
  prescribed integer
);

create table drug_attr (
  drug_id integer primary key,
  tekiyou text
);

create table gazou_label (
  conduct_id integer primary key,
  label text
);

create table hotline (
  hotline_id integer primary key autoincrement,
  message text,
  sender text,
  recipient text,
  posted_at text
);

create table intraclinic_comment (
  id integer primary key autoincrement,
  name text,
  content text,
  post_id integer,
  created_at text
);

create table intraclinic_post (
  id integer primary key autoincrement,
  content text,
  created_at text
);

create table intraclinic_tag (
  tag_id integer primary key autoincrement,
  name text
);

create table intraclinic_tag_post (
  tag_id integer,
  post_id integer,
  primary key(tag_id, post_id)
);

create table iyakuhin_master (
  iyakuhincode integer,
  name text,
  yomi text,
  unit text,
  yakka text,
  madoku text,
  kouhatsu text,
  zaikei text,
  valid_from text,
  valid_upto text,
  primary key(iyakuhincode, valid_from)
);

create table kizai_master (
  kizaicode integer,
  name text,
  yomi text,
  unit text,
  kingaku text,
  valid_from text,
  valid_upto text,
  primary key(kizaicode, valid_from)
);

create table kouhi (
  kouhi_id integer primary key autoincrement,
  patient_id integer,
  futansha integer,
  jukyuusha integer,
  valid_from text,
  valid_upto text
);

create table koukikourei (
  koukikourei_id integer primary key autoincrement,
  patient_id integer,
  hokensha_bangou integer,
  hihokensha_bangou integer,
  futan_wari integer,
  valid_from text,
  valid_upto text
);

create table patient (
  patient_id integer primary key autoincrement,
  last_name text,
  first_name text,
  last_name_yomi text,
  first_name_yomi text,
  sex text,
  birthday text,
  address text,
  phone text
);

create table payment (
  visit_id integer,
  amount integer,
  paytime text,
  primary key(visit_id, paytime)
);

create table pharma_drug (
  iyakuhincode integer primary key,
  description text,
  sideeffect text
);

create table pharma_queue (
  visit_id integer primary key,
  pharma_state integer
);

create table practice_log (
  serial_id integer primary key autoincrement,
  created_at text,
  kind text,
  body text
);

create table presc_example (
  presc_example_id integer primary key autoincrement,
  iyakuhincode integer,
  master_valid_from text,
  amount text,
  usage text,
  days integer,
  category integer,
  comment text
);

create table roujin (
  roujin_id integer primary key autoincrement,
  patient_id integer,
  shichouson integer,
  jukyuusha integer,
  futan_wari integer,
  valid_from text,
  valid_upto text
);

create table shahokokuho (
  shahokokuho_id integer primary key autoincrement,
  patient_id integer,
  hokensha_bangou integer,
  hihokensha_kigou text,
  hihokensha_bangou text,
  honnin integer,
  valid_from text,
  valid_upto text,
  kourei integer
);

create table shinryou (
  shinryou_id integer primary key autoincrement,
  visit_id integer,
  shinryoucode integer
);

create table shinryou_attr (
  shinryou_id integer primary key,
  tekiyou text
);

create table shinryou_master (
  shinryoucode integer,
  name text,
  tensuu text,
  tensuu_shikibetsu text,
  shuukeisaki text,
  houkatsukensa text,
  kensa_group text,
  valid_from text,
  valid_upto text,
  primary key(shinryoucode, valid_from)
);

create table shouki (
  visit_id integer primary key,
  shouki text
);

create table shuushokugo_master (
  shuushokugocode integer primary key,
  name text
);

create table text (
  text_id integer primary key autoincrement,
  visit_id integer,
  content text
);

create table visit (
  visit_id integer primary key autoincrement,
  patient_id integer,
  visited_at text,
  shahokokuho_id integer,
  roujin_id integer,
  koukikourei_id integer,
  kouhi1_id integer,
  kouhi2_id integer,
  kouhi3_id integer
);

create table wqueue (
  visit_id integer primary key,
  wait_state integer
);


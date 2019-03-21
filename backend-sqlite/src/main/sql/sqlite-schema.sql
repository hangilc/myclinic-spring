create table byoumei_master (
  shoubyoumeicode integer,
  name string,
  valid_from string,
  valid_upto string,
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
  amount real
);

create table conduct_kizai (
  conduct_kizai_id integer primary key autoincrement,
  conduct_id integer,
  kizaicode integer,
  amount real
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
  start_date string,
  end_date string,
  end_reason string
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
  amount real,
  usage string,
  days integer,
  category integer,
  prescribed integer
);

create table drug_attr (
  drug_id integer primary key,
  tekiyou string
);

create table gazou_label (
  conduct_id integer primary key,
  label string
);

create table hotline (
  hotline_id integer primary key autoincrement,
  message string,
  sender string,
  recipient string,
  posted_at string
);

create table intraclinic_comment (
  id integer primary key autoincrement,
  name string,
  content string,
  post_id integer,
  created_at string
);

create table intraclinic_post (
  id integer primary key autoincrement,
  content string,
  created_at string
);

create table intraclinic_tag (
  tag_id integer primary key autoincrement,
  name string
);

create table intraclinic_tag_post (
  tag_id integer,
  post_id integer,
  primary key(tag_id, post_id)
);

create table iyakuhin_master (
  iyakuhincode integer,
  valid_from string,
  name string,
  yomi string,
  unit string,
  yakka real,
  madoku string,
  kouhatsu string,
  zaikei string,
  valid_upto string,
  primary key(iyakuhincode, valid_from)
);

create table kizai_master (
  kizaicode integer,
  valid_from string,
  name string,
  yomi string,
  unit string,
  kingaku real,
  valid_upto string,
  primary key(kizaicode, valid_from)
);

create table kouhi (
  kouhi_id integer primary key autoincrement,
  patient_id integer,
  futansha integer,
  jukyuusha integer,
  valid_from string,
  valid_upto string
);

create table koukikourei (
  koukikourei_id integer primary key autoincrement,
  patient_id integer,
  hokensha_bangou string,
  hihokensha_bangou string,
  futan_wari integer,
  valid_from string,
  valid_upto string
);

create table patient (
  patient_id integer primary key autoincrement,
  last_name string,
  first_name string,
  last_name_yomi string,
  first_name_yomi string,
  birthday string,
  sex string,
  address string,
  phone string
);

create table payment (
  visit_id integer,
  amount integer,
  paytime string,
  primary key(visit_id, paytime)
);

create table pharma_drug (
  iyakuhincode integer primary key,
  description string,
  sideeffect string
);

create table pharma_queue (
  visit_id integer primary key,
  pharma_state integer
);

create table practice_log (
  serial_id integer primary key autoincrement,
  created_at string,
  kind string,
  body string
);

create table presc_example (
  presc_example_id integer primary key autoincrement,
  iyakuhincode integer,
  master_valid_from string,
  amount string,
  usage string,
  days integer,
  category integer,
  comment string
);

create table roujin (
  roujin_id integer primary key autoincrement,
  patient_id integer,
  shichouson integer,
  jukyuusha integer,
  futan_wari integer,
  valid_from string,
  valid_upto string
);

create table shahokokuho (
  shahokokuho_id integer primary key autoincrement,
  patient_id integer,
  hokensha_bangou integer,
  hihokensha_kigou string,
  hihokensha_bangou string,
  honnin integer,
  kourei integer,
  valid_from string,
  valid_upto string
);

create table shinryou (
  shinryou_id integer primary key autoincrement,
  visit_id integer,
  shinryoucode integer
);

create table shinryou_attr (
  shinryou_id integer primary key,
  tekiyou string
);

create table shinryou_master (
  shinryoucode integer,
  valid_from string,
  name string,
  tensuu integer,
  tensuu_shikibetsu string,
  shuukeisaki string,
  houkatsukensa string,
  oushinkubun string,
  kensa_group string,
  valid_upto string,
  primary key(shinryoucode, valid_from)
);

create table shouki (
  visit_id integer primary key,
  shouki string
);

create table shuushokugo_master (
  shuushokugocode integer primary key,
  name string
);

create table text (
  text_id integer primary key autoincrement,
  visit_id integer,
  content string
);

create table visit (
  visit_id integer primary key autoincrement,
  patient_id integer,
  visited_at string,
  shahokokuho_id integer,
  koukikourei_id integer,
  roujin_id integer,
  kouhi1_id integer,
  kouhi2_id integer,
  kouhi3_id integer
);

create table wqueue (
  visit_id integer primary key,
  wait_state integer
);

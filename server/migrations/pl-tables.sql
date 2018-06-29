
CREATE TABLE `pl_disease` (
  `pl_disease_id` int not null auto_increment,
  `disease_id` int(11) NOT NULL default '0',
  `patient_id` int(11) NOT NULL DEFAULT '0',
  `shoubyoumeicode` int(11) NOT NULL DEFAULT '0',
  `start_date` date NOT NULL DEFAULT '0000-00-00',
  `end_date` date NOT NULL DEFAULT '0000-00-00',
  `end_reason` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`pl_disease_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_disease_adj` (
  `pl_disease_adj_id` int not null auto_increment,
  `disease_adj_id` int(11) NOT NULL,
  `disease_id` int(11) NOT NULL DEFAULT '0',
  `shuushokugocode` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pl_disease_adj_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_hoken_koukikourei` (
  `pl_koukikourei_id` int(11) NOT NULL AUTO_INCREMENT,
  `koukikourei_id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL DEFAULT '0',
  `hokensha_bangou` varchar(255) NOT NULL DEFAULT '',
  `hihokensha_bangou` varchar(255) NOT NULL DEFAULT '',
  `futan_wari` tinyint(4) NOT NULL DEFAULT '0',
  `valid_from` date NOT NULL DEFAULT '0000-00-00',
  `valid_upto` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`pl_koukikourei_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_hoken_roujin` (
  `pl_roujin_id` int(11)  NOT NULL AUTO_INCREMENT,
  `roujin_id` int(11)  NOT NULL,
  `patient_id` int(11)  NOT NULL DEFAULT '0',
  `shichouson` int(11)  NOT NULL DEFAULT '0',
  `jukyuusha` int(11)  NOT NULL DEFAULT '0',
  `futan_wari` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `valid_from` date NOT NULL DEFAULT '0000-00-00',
  `valid_upto` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`pl_roujin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_hoken_shahokokuho` (
  `pl_shahokokuho_id` int(11) NOT NULL AUTO_INCREMENT,
  `shahokokuho_id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL DEFAULT '0',
  `hokensha_bangou` int(11) NOT NULL DEFAULT '0',
  `hihokensha_kigou` varchar(255) NOT NULL DEFAULT '',
  `hihokensha_bangou` varchar(255) NOT NULL DEFAULT '',
  `honnin` tinyint(4) NOT NULL DEFAULT '1',
  `valid_from` date NOT NULL DEFAULT '0000-00-00',
  `valid_upto` date NOT NULL DEFAULT '0000-00-00',
  `kourei` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pl_shahokokuho_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_hotline` (
  `pl_hotline_id` int(11) NOT NULL AUTO_INCREMENT,
  `hotline_id` int(11) NOT NULL,
  `message` text NOT NULL,
  `sender` varchar(255) NOT NULL DEFAULT '',
  `recipient` varchar(255) NOT NULL DEFAULT '',
  `m_datetime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`pl_hotline_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_kouhi` (
  `pl_kouhi_id` int(11) NOT NULL AUTO_INCREMENT,
  `kouhi_id` int(11) NOT NULL,
  `futansha` int(11) NOT NULL DEFAULT '0',
  `jukyuusha` int(11) NOT NULL DEFAULT '0',
  `valid_from` date NOT NULL DEFAULT '0000-00-00',
  `valid_upto` date NOT NULL DEFAULT '0000-00-00',
  `patient_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pl_kouhi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_patient` (
  `pl_patient_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(11) NOT NULL,
  `last_name` varchar(20) NOT NULL DEFAULT '',
  `first_name` varchar(20) NOT NULL DEFAULT '',
  `last_name_yomi` varchar(40) NOT NULL DEFAULT '',
  `first_name_yomi` varchar(40) NOT NULL DEFAULT '',
  `sex` char(1) NOT NULL DEFAULT '',
  `birth_day` date NOT NULL DEFAULT '0000-00-00',
  `address` varchar(80) NOT NULL DEFAULT '',
  `phone` varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (`pl_patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_pharma_drug` (
  `pl_iyakuhincode` int(11) NOT NULL DEFAULT '0',
  `iyakuhincode` int(11) NOT NULL DEFAULT '0',
  `description` text NOT NULL,
  `sideeffect` text NOT NULL,
  PRIMARY KEY (`pl_iyakuhincode`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_pharma_queue` (
  `pl_visit_id` int(11) NOT NULL DEFAULT '0',
  `visit_id` int(11) NOT NULL DEFAULT '0',
  `pharma_state` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`pl_visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;


CREATE TABLE `pl_visit` (
  `pl_visit_id` int(11) NOT NULL AUTO_INCREMENT,
  `visit_id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL DEFAULT '0',
  `v_datetime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `shahokokuho_id` int(11) NOT NULL DEFAULT '0',
  `roujin_id` int(11) NOT NULL DEFAULT '0',
  `kouhi_1_id` int(11) NOT NULL DEFAULT '0',
  `kouhi_2_id` int(11) NOT NULL DEFAULT '0',
  `kouhi_3_id` int(11) NOT NULL DEFAULT '0',
  `koukikourei_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pl_visit_id`),
  KEY `patient_id` (`patient_id`),
  KEY `idx_v_datetime` (`v_datetime`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_visit_charge` (
  `pl_visit_id` int(11) NOT NULL DEFAULT '0',
  `visit_id` int(11) NOT NULL DEFAULT '0',
  `charge` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pl_visit_id`),
  KEY `visit_id` (`visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_visit_conduct` (
  `pl_id` int(11) NOT NULL AUTO_INCREMENT,
  `id` int(11) NOT NULL,
  `visit_id` int(11) NOT NULL DEFAULT '0',
  `kind` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pl_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_visit_conduct_drug` (
  `pl_id` int(11) NOT NULL AUTO_INCREMENT,
  `id` int(11) NOT NULL,
  `visit_conduct_id` int(11) NOT NULL DEFAULT '0',
  `iyakuhincode` int(11) NOT NULL DEFAULT '0',
  `amount` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`pl_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_visit_conduct_kizai` (
  `pl_id` int(11) NOT NULL AUTO_INCREMENT,
  `id` int(11) NOT NULL,
  `visit_conduct_id` int(11) NOT NULL DEFAULT '0',
  `kizaicode` int(11) NOT NULL DEFAULT '0',
  `amount` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`pl_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_visit_conduct_shinryou` (
  `pl_id` int(11) NOT NULL AUTO_INCREMENT,
  `id` int(11) NOT NULL,
  `visit_conduct_id` int(11) NOT NULL DEFAULT '0',
  `shinryoucode` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pl_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_visit_drug` (
  `pl_drug_id` int(11) NOT NULL AUTO_INCREMENT,
  `drug_id` int(11) NOT NULL,
  `visit_id` int(11) NOT NULL DEFAULT '0',
  `d_iyakuhincode` int(11) NOT NULL DEFAULT '0',
  `d_amount` varchar(10) NOT NULL DEFAULT '',
  `d_usage` varchar(255) NOT NULL DEFAULT '',
  `d_days` int(11) NOT NULL DEFAULT '0',
  `d_category` tinyint(4) NOT NULL DEFAULT '0',
  `d_prescribed` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pl_drug_id`),
  KEY `visit_id` (`visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_visit_gazou_label` (
  `pl_visit_conduct_id` int(11) NOT NULL DEFAULT '0',
  `visit_conduct_id` int(11) NOT NULL DEFAULT '0',
  `label` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pl_visit_conduct_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_visit_payment` (
  `pl_visit_id` int(11) NOT NULL DEFAULT '0',
  `visit_id` int(11) NOT NULL DEFAULT '0',
  `amount` int(11) DEFAULT NULL,
  `paytime` datetime DEFAULT NULL,
  KEY `visit_id` (`visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_visit_shinryou` (
  `pl_shinryou_id` int(11) NOT NULL AUTO_INCREMENT,
  `shinryou_id` int(11) NOT NULL,
  `visit_id` int(11) NOT NULL DEFAULT '0',
  `shinryoucode` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pl_shinryou_id`),
  KEY `visit_id` (`visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_visit_text` (
  `pl_text_id` int(11) NOT NULL AUTO_INCREMENT,
  `text_id` int(11) NOT NULL,
  `visit_id` int(11) DEFAULT NULL,
  `content` text NOT NULL,
  PRIMARY KEY (`pl_text_id`),
  KEY `visit_id` (`visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

CREATE TABLE `pl_wqueue` (
  `pl_visit_id` int(11) NOT NULL DEFAULT '0',
  `visit_id` int(11) NOT NULL DEFAULT '0',
  `wait_state` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pl_visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;


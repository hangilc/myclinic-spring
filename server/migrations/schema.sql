-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: myclinic
-- ------------------------------------------------------
-- Server version	5.7.12-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `myclinic`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `myclinic` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `myclinic`;

--
-- Table structure for table `disease`
--

DROP TABLE IF EXISTS `disease`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `disease` (
  `disease_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `patient_id` int(10) unsigned NOT NULL DEFAULT '0',
  `shoubyoumeicode` int(11) NOT NULL DEFAULT '0',
  `start_date` date NOT NULL DEFAULT '0000-00-00',
  `end_date` date NOT NULL DEFAULT '0000-00-00',
  `end_reason` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`disease_id`)
) ENGINE=InnoDB AUTO_INCREMENT=57714 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `disease_adj`
--

DROP TABLE IF EXISTS `disease_adj`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `disease_adj` (
  `disease_adj_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `disease_id` int(11) NOT NULL DEFAULT '0',
  `shuushokugocode` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`disease_adj_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9101 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hoken_koukikourei`
--

DROP TABLE IF EXISTS `hoken_koukikourei`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hoken_koukikourei` (
  `koukikourei_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(11) NOT NULL DEFAULT '0',
  `hokensha_bangou` varchar(255) NOT NULL DEFAULT '',
  `hihokensha_bangou` varchar(255) NOT NULL DEFAULT '',
  `futan_wari` tinyint(4) NOT NULL DEFAULT '0',
  `valid_from` date NOT NULL DEFAULT '0000-00-00',
  `valid_upto` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`koukikourei_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1510 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hoken_roujin`
--

DROP TABLE IF EXISTS `hoken_roujin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hoken_roujin` (
  `roujin_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `patient_id` int(10) unsigned NOT NULL DEFAULT '0',
  `shichouson` int(10) unsigned NOT NULL DEFAULT '0',
  `jukyuusha` int(10) unsigned NOT NULL DEFAULT '0',
  `futan_wari` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `valid_from` date NOT NULL DEFAULT '0000-00-00',
  `valid_upto` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`roujin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=193 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hoken_shahokokuho`
--

DROP TABLE IF EXISTS `hoken_shahokokuho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hoken_shahokokuho` (
  `shahokokuho_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `patient_id` int(10) unsigned NOT NULL DEFAULT '0',
  `hokensha_bangou` int(10) unsigned NOT NULL DEFAULT '0',
  `hihokensha_kigou` varchar(255) NOT NULL DEFAULT '',
  `hihokensha_bangou` varchar(255) NOT NULL DEFAULT '',
  `honnin` tinyint(4) NOT NULL DEFAULT '1',
  `valid_from` date NOT NULL DEFAULT '0000-00-00',
  `valid_upto` date NOT NULL DEFAULT '0000-00-00',
  `kourei` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`shahokokuho_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10474 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hotline`
--

DROP TABLE IF EXISTS `hotline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hotline` (
  `hotline_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `message` text NOT NULL,
  `sender` varchar(255) NOT NULL DEFAULT '',
  `recipient` varchar(255) NOT NULL DEFAULT '',
  `m_datetime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`hotline_id`)
) ENGINE=InnoDB AUTO_INCREMENT=208962 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iyakuhin_master_arch`
--

DROP TABLE IF EXISTS `iyakuhin_master_arch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iyakuhin_master_arch` (
  `iyakuhincode` int(10) unsigned NOT NULL DEFAULT '0',
  `yakkacode` varchar(12) NOT NULL DEFAULT '',
  `name` varchar(64) NOT NULL DEFAULT '',
  `yomi` varchar(20) NOT NULL DEFAULT '',
  `unit` varchar(12) NOT NULL DEFAULT '',
  `yakka` varchar(10) NOT NULL DEFAULT '',
  `madoku` char(1) NOT NULL DEFAULT '',
  `kouhatsu` char(1) NOT NULL DEFAULT '',
  `zaikei` char(1) NOT NULL DEFAULT '',
  `valid_from` date NOT NULL DEFAULT '0000-00-00',
  `valid_upto` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`iyakuhincode`,`valid_from`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `kouhi`
--

DROP TABLE IF EXISTS `kouhi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kouhi` (
  `kouhi_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `futansha` int(10) unsigned NOT NULL DEFAULT '0',
  `jukyuusha` int(10) unsigned NOT NULL DEFAULT '0',
  `valid_from` date NOT NULL DEFAULT '0000-00-00',
  `valid_upto` date NOT NULL DEFAULT '0000-00-00',
  `patient_id` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`kouhi_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1320 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient` (
  `patient_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `last_name` varchar(20) NOT NULL DEFAULT '',
  `first_name` varchar(20) NOT NULL DEFAULT '',
  `last_name_yomi` varchar(40) NOT NULL DEFAULT '',
  `first_name_yomi` varchar(40) NOT NULL DEFAULT '',
  `sex` char(1) NOT NULL DEFAULT '',
  `birth_day` date NOT NULL DEFAULT '0000-00-00',
  `address` varchar(80) NOT NULL DEFAULT '',
  `phone` varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (`patient_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6946 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pharma_drug`
--

DROP TABLE IF EXISTS `pharma_drug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pharma_drug` (
  `iyakuhincode` int(10) unsigned NOT NULL DEFAULT '0',
  `description` text NOT NULL,
  `sideeffect` text NOT NULL,
  PRIMARY KEY (`iyakuhincode`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pharma_queue`
--

DROP TABLE IF EXISTS `pharma_queue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pharma_queue` (
  `visit_id` int(10) unsigned NOT NULL DEFAULT '0',
  `pharma_state` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `practice_log`
--

DROP TABLE IF EXISTS `practice_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `practice_log` (
  `practice_log_id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `kind` varchar(32) DEFAULT NULL,
  `body` json DEFAULT NULL,
  PRIMARY KEY (`practice_log_id`),
  KEY `practice_date` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=7831 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `presc_example`
--

DROP TABLE IF EXISTS `presc_example`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `presc_example` (
  `presc_example_id` int(11) NOT NULL AUTO_INCREMENT,
  `m_iyakuhincode` int(11) NOT NULL DEFAULT '0',
  `m_master_valid_from` date NOT NULL DEFAULT '0000-00-00',
  `m_amount` varchar(10) NOT NULL DEFAULT '',
  `m_usage` varchar(255) NOT NULL DEFAULT '',
  `m_days` int(10) NOT NULL DEFAULT '0',
  `m_category` tinyint(4) NOT NULL DEFAULT '0',
  `m_comment` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`presc_example_id`)
) ENGINE=InnoDB AUTO_INCREMENT=866 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shinryoukoui_master_arch`
--

DROP TABLE IF EXISTS `shinryoukoui_master_arch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shinryoukoui_master_arch` (
  `shinryoucode` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(64) NOT NULL DEFAULT '',
  `tensuu` varchar(10) NOT NULL DEFAULT '',
  `tensuu_shikibetsu` char(1) NOT NULL DEFAULT '',
  `shuukeisaki` varchar(3) NOT NULL DEFAULT '',
  `houkatsukensa` varchar(2) NOT NULL DEFAULT '',
  `oushinkubun` char(1) NOT NULL DEFAULT '',
  `kensagroup` varchar(2) NOT NULL DEFAULT '',
  `valid_from` date NOT NULL DEFAULT '0000-00-00',
  `valid_upto` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`shinryoucode`,`valid_from`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shoubyoumei_master_arch`
--

DROP TABLE IF EXISTS `shoubyoumei_master_arch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shoubyoumei_master_arch` (
  `shoubyoumeicode` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(60) NOT NULL DEFAULT '',
  `valid_from` date NOT NULL DEFAULT '0000-00-00',
  `valid_upto` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`shoubyoumeicode`,`valid_from`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shuushokugo_master`
--

DROP TABLE IF EXISTS `shuushokugo_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shuushokugo_master` (
  `shuushokugocode` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(40) NOT NULL DEFAULT '',
  PRIMARY KEY (`shuushokugocode`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_drug`
--

DROP TABLE IF EXISTS `stock_drug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock_drug` (
  `stock_drug_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `m_iyakuhincode` int(10) unsigned NOT NULL DEFAULT '0',
  `m_amount` varchar(10) NOT NULL DEFAULT '',
  `m_usage` varchar(255) NOT NULL DEFAULT '',
  `m_days` int(10) unsigned NOT NULL DEFAULT '0',
  `m_category` tinyint(4) NOT NULL DEFAULT '0',
  `m_comment` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`stock_drug_id`)
) ENGINE=InnoDB AUTO_INCREMENT=313 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tokuteikizai_master_arch`
--

DROP TABLE IF EXISTS `tokuteikizai_master_arch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tokuteikizai_master_arch` (
  `kizaicode` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(64) NOT NULL DEFAULT '',
  `yomi` varchar(20) NOT NULL DEFAULT '',
  `unit` varchar(12) NOT NULL DEFAULT '',
  `kingaku` varchar(10) NOT NULL DEFAULT '',
  `valid_from` date NOT NULL DEFAULT '0000-00-00',
  `valid_upto` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`kizaicode`,`valid_from`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visit`
--

DROP TABLE IF EXISTS `visit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit` (
  `visit_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `patient_id` int(10) unsigned NOT NULL DEFAULT '0',
  `v_datetime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `shahokokuho_id` int(10) unsigned NOT NULL DEFAULT '0',
  `roujin_id` int(10) unsigned NOT NULL DEFAULT '0',
  `kouhi_1_id` int(10) unsigned NOT NULL DEFAULT '0',
  `kouhi_2_id` int(10) unsigned NOT NULL DEFAULT '0',
  `kouhi_3_id` int(10) unsigned NOT NULL DEFAULT '0',
  `koukikourei_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`visit_id`),
  KEY `patient_id` (`patient_id`),
  KEY `idx_v_datetime` (`v_datetime`)
) ENGINE=InnoDB AUTO_INCREMENT=91703 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visit_charge`
--

DROP TABLE IF EXISTS `visit_charge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_charge` (
  `visit_id` int(10) unsigned NOT NULL DEFAULT '0',
  `charge` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`visit_id`),
  KEY `visit_id` (`visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visit_conduct`
--

DROP TABLE IF EXISTS `visit_conduct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_conduct` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `visit_id` int(10) unsigned NOT NULL DEFAULT '0',
  `kind` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1160 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visit_conduct_drug`
--

DROP TABLE IF EXISTS `visit_conduct_drug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_conduct_drug` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `visit_conduct_id` int(10) unsigned NOT NULL DEFAULT '0',
  `iyakuhincode` int(10) unsigned NOT NULL DEFAULT '0',
  `amount` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=440 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visit_conduct_kizai`
--

DROP TABLE IF EXISTS `visit_conduct_kizai`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_conduct_kizai` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `visit_conduct_id` int(10) unsigned NOT NULL DEFAULT '0',
  `kizaicode` int(10) unsigned NOT NULL DEFAULT '0',
  `amount` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=744 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visit_conduct_shinryou`
--

DROP TABLE IF EXISTS `visit_conduct_shinryou`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_conduct_shinryou` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `visit_conduct_id` int(10) unsigned NOT NULL DEFAULT '0',
  `shinryoucode` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1717 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visit_drug`
--

DROP TABLE IF EXISTS `visit_drug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_drug` (
  `drug_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `visit_id` int(10) unsigned NOT NULL DEFAULT '0',
  `d_iyakuhincode` int(10) unsigned NOT NULL DEFAULT '0',
  `d_amount` varchar(10) NOT NULL DEFAULT '',
  `d_usage` varchar(255) NOT NULL DEFAULT '',
  `d_days` int(10) unsigned NOT NULL DEFAULT '0',
  `d_category` tinyint(4) NOT NULL DEFAULT '0',
  `d_prescribed` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`drug_id`),
  KEY `visit_id` (`visit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=174179 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visit_gazou_label`
--

DROP TABLE IF EXISTS `visit_gazou_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_gazou_label` (
  `visit_conduct_id` int(10) unsigned NOT NULL DEFAULT '0',
  `label` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`visit_conduct_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visit_payment`
--

DROP TABLE IF EXISTS `visit_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_payment` (
  `visit_id` int(10) unsigned NOT NULL DEFAULT '0',
  `amount` int(10) unsigned DEFAULT NULL,
  `paytime` datetime DEFAULT NULL,
  KEY `visit_id` (`visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visit_shinryou`
--

DROP TABLE IF EXISTS `visit_shinryou`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_shinryou` (
  `shinryou_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `visit_id` int(10) unsigned NOT NULL DEFAULT '0',
  `shinryoucode` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`shinryou_id`),
  KEY `visit_id` (`visit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=607321 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visit_text`
--

DROP TABLE IF EXISTS `visit_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_text` (
  `text_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `visit_id` int(10) unsigned DEFAULT NULL,
  `content` text NOT NULL,
  PRIMARY KEY (`text_id`),
  KEY `visit_id` (`visit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=112262 DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wqueue`
--

DROP TABLE IF EXISTS `wqueue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wqueue` (
  `visit_id` int(10) unsigned NOT NULL DEFAULT '0',
  `wait_state` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Current Database: `intraclinic`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `intraclinic` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `intraclinic`;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `content` varchar(255) NOT NULL DEFAULT '',
  `post_id` int(11) NOT NULL DEFAULT '0',
  `created_at` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`id`),
  KEY `fk_comment_post` (`post_id`),
  CONSTRAINT `fk_comment_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2218 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `created_at` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1134 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tag_post`
--

DROP TABLE IF EXISTS `tag_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag_post` (
  `post_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  PRIMARY KEY (`post_id`,`tag_id`),
  KEY `tag_id` (`tag_id`),
  CONSTRAINT `tag_post_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `tag_post_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-07-04 15:35:49

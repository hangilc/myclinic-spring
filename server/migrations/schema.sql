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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `drug_attr`
--

DROP TABLE IF EXISTS `drug_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug_attr` (
  `drug_id` int(10) unsigned NOT NULL,
  `tekiyou` text,
  PRIMARY KEY (`drug_id`),
  CONSTRAINT `drug_attr_ibfk_1` FOREIGN KEY (`drug_id`) REFERENCES `visit_drug` (`drug_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `intraclinic_comment`
--

DROP TABLE IF EXISTS `intraclinic_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `intraclinic_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `content` varchar(255) NOT NULL DEFAULT '',
  `post_id` int(11) NOT NULL DEFAULT '0',
  `created_at` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `intraclinic_post`
--

DROP TABLE IF EXISTS `intraclinic_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `intraclinic_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `created_at` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `intraclinic_tag`
--

DROP TABLE IF EXISTS `intraclinic_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `intraclinic_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `intraclinic_tag_post`
--

DROP TABLE IF EXISTS `intraclinic_tag_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `intraclinic_tag_post` (
  `post_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shinryou_attr`
--

DROP TABLE IF EXISTS `shinryou_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shinryou_attr` (
  `shinryou_id` int(10) unsigned NOT NULL,
  `tekiyou` text,
  PRIMARY KEY (`shinryou_id`),
  CONSTRAINT `shinryou_attr_ibfk_1` FOREIGN KEY (`shinryou_id`) REFERENCES `visit_shinryou` (`shinryou_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `shinryou_view`
--

DROP TABLE IF EXISTS `shinryou_view`;
/*!50001 DROP VIEW IF EXISTS `shinryou_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `shinryou_view` AS SELECT 
 1 AS `shinryou_id`,
 1 AS `visit_id`,
 1 AS `visited_at`,
 1 AS `name`,
 1 AS `shinryoucode`*/;
SET character_set_client = @saved_cs_client;

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
-- Table structure for table `shouki`
--

DROP TABLE IF EXISTS `shouki`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shouki` (
  `visit_id` int(10) unsigned NOT NULL,
  `shouki` text,
  PRIMARY KEY (`visit_id`),
  CONSTRAINT `shouki_ibfk_1` FOREIGN KEY (`visit_id`) REFERENCES `visit` (`visit_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
) ENGINE=InnoDB  DEFAULT CHARSET=cp932;
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
-- Final view structure for view `shinryou_view`
--

/*!50001 DROP VIEW IF EXISTS `shinryou_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = cp932 */;
/*!50001 SET character_set_results     = cp932 */;
/*!50001 SET collation_connection      = cp932_japanese_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`hangil`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `shinryou_view` AS select `s`.`shinryou_id` AS `shinryou_id`,`v`.`visit_id` AS `visit_id`,`v`.`v_datetime` AS `visited_at`,`m`.`name` AS `name`,`s`.`shinryoucode` AS `shinryoucode` from ((`visit_shinryou` `s` join `visit` `v`) join `shinryoukoui_master_arch` `m`) where ((`s`.`visit_id` = `v`.`visit_id`) and (`m`.`shinryoucode` = `s`.`shinryoucode`) and (`m`.`valid_from` <= cast(`v`.`v_datetime` as date)) and ((`m`.`valid_upto` = '0000-00-00') or (`m`.`valid_upto` >= cast(`v`.`v_datetime` as date)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-29 17:26:13

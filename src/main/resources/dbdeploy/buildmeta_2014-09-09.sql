# Sequel Pro dump
# Version 2492
# http://code.google.com/p/sequel-pro
#
# Host: 127.0.0.1 (MySQL 5.0.89)
# Database: buildmeta
# Generation Time: 2014-09-09 06:24:35 +0000
# ************************************************************

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table branch
# ------------------------------------------------------------

DROP TABLE IF EXISTS `branch`;

CREATE TABLE `branch` (
  `id` int(11) NOT NULL auto_increment,
  `repo_url` varchar(256) NOT NULL,
  `name` varchar(64) NOT NULL,
  `created` bigint(16) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table build
# ------------------------------------------------------------

DROP TABLE IF EXISTS `build`;

CREATE TABLE `build` (
  `id` int(11) NOT NULL auto_increment,
  `repo_url` varchar(256) NOT NULL,
  `branch` varchar(64) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `artifact_id` varchar(64) NOT NULL,
  `packaging` varchar(64) NOT NULL,
  `type` varchar(64) NOT NULL,
  `version` varchar(64) NOT NULL,
  `md5` varchar(64) NOT NULL,
  `download_url` varchar(256) NOT NULL,
  `classifier` varchar(64) default NULL,
  `created` bigint(16) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table repository
# ------------------------------------------------------------

DROP TABLE IF EXISTS `repository`;

CREATE TABLE `repository` (
  `id` int(11) NOT NULL auto_increment,
  `repo_url` varchar(256) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `collector`;

CREATE TABLE `collector` (
  `id` int(11) NOT NULL auto_increment,
  `scalr_url` varchar(256) NOT NULL,
  `env_id` varchar(256) NOT NULL,
  `role` varchar(256) NOT NULL,
  `target` varchar(256) NOT NULL,
  `c_type` varchar(256) NOT NULL,
  `content` MediumText NOT NULL,
  `expect` MediumText NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO collector VALUES ('1','test.cloudfarms.net', '71', 'bst-devops-buildmeta-64-centos6-1028', 'system env','text','env','');
INSERT INTO collector VALUES ('2', 'test.cloudfarms.net','71', 'bst-devops-buildmeta-64-centos6-1028', 'system env','text','cat /opt/buildmeta/src/main/resources/log4j.properties','');

CREATE TABLE IF NOT EXISTS `farm` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scalr_farm_name` varchar(100) NOT NULL DEFAULT '',
  `scalr_farm_id` varchar(20) NOT NULL DEFAULT '',
  `scalr_env_id` varchar(20) NOT NULL DEFAULT '',
  `scalr_endpoint` varchar(100) NOT NULL DEFAULT '',
  `system_name` varchar(50) NOT NULL DEFAULT '',
  `register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `current_version` varchar(200) DEFAULT '',
  `account_id` int(11) DEFAULT '1',
  `monitoring_interval` varchar(20) DEFAULT '3600',
  `farm_type` varchar(100) NOT NULL DEFAULT '',
  `jenkins_job_name` varchar(100) DEFAULT '',
  `is_running` tinyint(1) DEFAULT NULL,
  `scalr_env_name` varchar(20) DEFAULT '',
  `system_type` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=30 ;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

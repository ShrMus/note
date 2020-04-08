/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50027
Source Host           : localhost:3306
Source Database       : kavo

Target Server Type    : MYSQL
Target Server Version : 50027
File Encoding         : 65001

Date: 2020-04-08 16:49:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for kavo_forum
-- ----------------------------
DROP TABLE IF EXISTS `kavo_forum`;
CREATE TABLE `kavo_forum` (
  `forum_id` int(11) NOT NULL auto_increment,
  `forum_user_id` int(11) default NULL,
  `forum_title` varchar(255) default NULL,
  `forum_content` varchar(255) default NULL,
  `forum_publishtime` varchar(255) default NULL,
  PRIMARY KEY  (`forum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for kavo_leave_message
-- ----------------------------
DROP TABLE IF EXISTS `kavo_leave_message`;
CREATE TABLE `kavo_leave_message` (
  `leave_message_id` int(11) NOT NULL auto_increment,
  `leave_message_share_id` int(11) default NULL,
  `leave_message_user_id_reply` int(11) default NULL,
  `leave_message_parentid` int(11) default NULL,
  `leave_message_user_id_author` int(11) default NULL,
  `leave_message_content` varchar(255) default NULL,
  `leave_message_publishtime` date default NULL,
  PRIMARY KEY  (`leave_message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for kavo_news
-- ----------------------------
DROP TABLE IF EXISTS `kavo_news`;
CREATE TABLE `kavo_news` (
  `news_id` int(11) NOT NULL auto_increment,
  `news_title` varchar(255) default NULL,
  `news_content` varchar(255) default NULL,
  `news_publishtime` varchar(255) default NULL,
  `newstype_id` int(11) default NULL,
  `newstype_name` varchar(255) default NULL,
  `newstype_description` varchar(255) default NULL,
  PRIMARY KEY  (`news_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for kavo_newstype
-- ----------------------------
DROP TABLE IF EXISTS `kavo_newstype`;
CREATE TABLE `kavo_newstype` (
  `newstype_id` int(11) NOT NULL auto_increment,
  `newstype_name` varchar(255) default NULL,
  `newstype_description` varchar(255) default NULL,
  PRIMARY KEY  (`newstype_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for kavo_share
-- ----------------------------
DROP TABLE IF EXISTS `kavo_share`;
CREATE TABLE `kavo_share` (
  `share_id` int(11) NOT NULL auto_increment,
  `share_user_id` int(11) default NULL,
  `share_word` varchar(255) default NULL,
  `share_image` varchar(255) default NULL,
  `share_uploadtime` varchar(255) default NULL,
  `share_great` varchar(255) default NULL,
  `share_user_openid` varchar(255) default NULL,
  `share_ispass` varchar(255) default NULL,
  PRIMARY KEY  (`share_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for kavo_user
-- ----------------------------
DROP TABLE IF EXISTS `kavo_user`;
CREATE TABLE `kavo_user` (
  `user_id` int(11) NOT NULL auto_increment,
  `user_name` varchar(255) default NULL,
  `user_hospital` varchar(255) default NULL,
  `user_phone` varchar(255) default NULL,
  `user_zone` varchar(255) default NULL,
  `user_time` varchar(255) default NULL,
  `user_from` varchar(255) default NULL,
  `user_openid` varchar(255) default NULL,
  `user_nickname` varchar(255) default NULL,
  `user_headimgurl` varchar(255) default NULL,
  `user_vote_num` varchar(255) default NULL,
  `user_description` varchar(255) default NULL,
  `user_poImgUrl` varchar(255) default NULL,
  `user_poUrl` varchar(255) default NULL,
  `user_nameUrl` varchar(255) default NULL,
  PRIMARY KEY  (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for kavo_yuyue
-- ----------------------------
DROP TABLE IF EXISTS `kavo_yuyue`;
CREATE TABLE `kavo_yuyue` (
  `yuyue_id` int(11) NOT NULL,
  `yuyue_name` varchar(255) default NULL,
  `yuyue_hospital` varchar(255) default NULL,
  `yuyue_oph` varchar(255) default NULL,
  `yuyue_zone` varchar(255) default NULL,
  `yuyue_time` varchar(255) default NULL,
  `yuyue_ptel` varchar(255) default NULL,
  `yuyue_code` varchar(255) default NULL,
  PRIMARY KEY  (`yuyue_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

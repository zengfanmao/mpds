/*
Navicat MySQL Data Transfer

Source Server         : 114.115.165.67
Source Server Version : 50640
Source Host           : 114.115.165.67:3306
Source Database       : mpds_new

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2018-09-09 13:11:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mpds_group_user
-- ----------------------------
DROP TABLE IF EXISTS `mpds_group_user`;
CREATE TABLE `mpds_group_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupName` varchar(64) DEFAULT NULL COMMENT '组名',
  `groupid` varchar(10) DEFAULT NULL COMMENT '组编号',
  `type` char(3) DEFAULT NULL COMMENT 'PDT；APP',
  `account` varchar(10) DEFAULT NULL COMMENT '关联用户编号',
  `name` varchar(32) DEFAULT NULL COMMENT '关联用户名',
  `dept` varchar(64) DEFAULT NULL COMMENT '部门',
  `deptid` int(11) DEFAULT NULL COMMENT '部门ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户关联组';

-- ----------------------------
-- Records of mpds_group_user
-- ----------------------------
INSERT INTO `mpds_group_user` VALUES ('1', '10009', '10009', 'APP', 'admin', 'admin', '123', '123', '2018-09-01 16:51:30', '2018-09-01 16:51:33');

/*
 Navicat Premium Data Transfer

 Source Server         : 47.94.104.229_3306
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : 47.94.104.229:3306
 Source Schema         : dbchat

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 28/08/2018 09:35:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbapitokens
-- ----------------------------
DROP TABLE IF EXISTS `tbapitokens`;
CREATE TABLE `tbapitokens`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `uCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户编号',
  `apiToken` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密钥',
  `createdTime` timestamp(0) NULL DEFAULT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Api访问密钥' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbdiscussiongroups
-- ----------------------------
DROP TABLE IF EXISTS `tbdiscussiongroups`;
CREATE TABLE `tbdiscussiongroups`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `isDel` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除',
  `recSN` blob NOT NULL,
  `createdTime` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `discussionCode` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '讨论组编号',
  `discussionName` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '讨论组名字',
  `createdUserCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `createdUserName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建时间',
  `csCode` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '讨论组' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbdiscussiongroupmenbers
-- ----------------------------
DROP TABLE IF EXISTS `tbdiscussiongroupmenbers`;
CREATE TABLE `tbdiscussiongroupmenbers`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `createdTime` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `discussionCode` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '讨论组编号',
  `createdUserCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人编号',
  `createdUserName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `uCode` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '讨论组成员',
  `csCode` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '讨论组成员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcasemessages
-- ----------------------------
DROP TABLE IF EXISTS `tbcasemessages`;
CREATE TABLE `tbcasemessages`  (
  `isDel` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除',
  `recSN` blob NULL,
  `csCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `msgType` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息类型Text，Image，Audio，Video，Map等',
  `msgAbstract` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '消息内容，文字内容',
  `msgFile` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息相关文件id，如发送图片的文件id',
  `uCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发送用户编号',
  `msgTime` timestamp(0) NULL DEFAULT NULL COMMENT '发送时间',
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息主键',
  `uLatitude` decimal(10, 0) NULL DEFAULT NULL COMMENT '发送人当前纬度',
  `uLongitude` decimal(10, 0) NULL DEFAULT NULL COMMENT '发送人当前经度',
  `uPositionName` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发送人当前位置',
  `msgFromType` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息来源,Group,Person,LivingRoom等',
  `msgToCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发送给谁，讨论组则讨论组id，个人则是个人编号，直播室则是直播室id',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息内容' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbfiledetails
-- ----------------------------
DROP TABLE IF EXISTS `tbfiledetails`;
CREATE TABLE `tbfiledetails`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `attRsCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `fCode` char(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件编号',
  `fName` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名字',
  `fSize` bigint(20) NULL DEFAULT NULL COMMENT '文件大小',
  `fAbstract` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容描述',
  `fFirstFrame` char(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '视频的第一帧图片',
  `fStartTime` timestamp(0) NULL DEFAULT NULL COMMENT '语音开始时间',
  `fEndTime` timestamp(0) NULL DEFAULT NULL COMMENT '语音结束时间',
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fRelativePath` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件存放地址',
  `virtualId` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '虚拟id，比如关联讨论组，可以根据id查所有讨论组或者其他的的文件',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息内容相关文件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbusers
-- ----------------------------
DROP TABLE IF EXISTS `tbusers`;
CREATE TABLE `tbusers`  (
  `isDel` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除',
  `recSN` blob NULL,
  `uCode` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户编号',
  `uBelong` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uIsActive` tinyint(1) NULL DEFAULT NULL COMMENT '是否激活',
  `rName` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pcNum` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '警员编号',
  `uName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `uSex` int(11) NULL DEFAULT NULL COMMENT '性别',
  `uDuty` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职位',
  `dCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uTel` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uShortNum` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话号码',
  `uPassword` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `uHeadPortrait` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `dName` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门',
  `LYCID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `loginFailTimes` smallint(6) NULL DEFAULT NULL,
  `lastLoginTime` timestamp(0) NULL DEFAULT NULL,
  `uRemarks` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `Createtime` timestamp(0) NULL DEFAULT NULL,
  `uDepartment` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `accountType` smallint(6) NULL DEFAULT NULL COMMENT '账号可类型',
  `uEmployeenum` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uIshistory` tinyint(1) NULL DEFAULT NULL,
  `uIsUnilt` tinyint(1) NULL DEFAULT NULL,
  `uIsAccontion` tinyint(1) NULL DEFAULT NULL,
  `uUnitCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '单位编号',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS `tbcasegps`;
CREATE TABLE `tbcasegps` (
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `csCode` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `gpsTargetType` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '定位类型，是什么设备定位',
  `devCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '定位设备编号',
  `uCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '使用用户编号',
  `gpsLongitude` decimal(10,0) DEFAULT NULL COMMENT '经度',
  `gpsLatitude` decimal(10,0) DEFAULT NULL COMMENT '纬度',
  `gpsTime` timestamp NULL DEFAULT NULL COMMENT '定位时间',
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `altitude` decimal(10,0) unsigned DEFAULT NULL COMMENT '海拔高度',
  `speed` decimal(10,0) DEFAULT NULL COMMENT '速度',
  `accelerationX` decimal(10,0) DEFAULT NULL COMMENT 'x轴加速度',
  `accelerationY` decimal(10,0) DEFAULT NULL COMMENT 'y轴加速度',
  `accelerationZ` decimal(10,0) DEFAULT NULL COMMENT 'z轴加速度',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

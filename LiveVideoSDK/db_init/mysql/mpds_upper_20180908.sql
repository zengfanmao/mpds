/*
Navicat MySQL Data Transfer

Source Server         : MySQL_Local
Source Server Version : 50622
Source Host           : 127.0.0.1:3307
Source Database       : mpds_upper

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2018-09-08 18:53:36
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户关联组';

-- ----------------------------
-- Records of mpds_group_user
-- ----------------------------

-- ----------------------------
-- Table structure for mpds_log
-- ----------------------------
DROP TABLE IF EXISTS `mpds_log`;
CREATE TABLE `mpds_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(10) DEFAULT NULL COMMENT '用户编号',
  `name` varchar(32) DEFAULT NULL COMMENT '用户名',
  `eventName` varchar(32) DEFAULT NULL COMMENT '操作/事件',
  `remark` varchar(512) DEFAULT NULL COMMENT '操作内容',
  `status` char(1) DEFAULT NULL COMMENT '0-成功，1-失败',
  `type` varchar(10) DEFAULT NULL COMMENT 'APP、Console、PDT',
  `user_ip` varchar(16) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='管理日志表';

-- ----------------------------
-- Records of mpds_log
-- ----------------------------

-- ----------------------------
-- Table structure for mpds_menu
-- ----------------------------
DROP TABLE IF EXISTS `mpds_menu`;
CREATE TABLE `mpds_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL COMMENT '所属父导航ID',
  `nav_type` varchar(50) DEFAULT NULL COMMENT '导航类别',
  `name` varchar(50) DEFAULT NULL COMMENT '导航ID',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `sub_title` varchar(100) DEFAULT NULL COMMENT '副标题',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '图标地址',
  `link_url` varchar(255) DEFAULT NULL COMMENT '链接地址',
  `sort_id` int(11) DEFAULT NULL COMMENT '排序数字',
  `is_lock` tinyint(4) DEFAULT NULL COMMENT '是否隐藏0显示1隐藏',
  `remark` text COMMENT '备注说明',
  `action_type` text COMMENT '权限资源',
  `is_sys` tinyint(4) DEFAULT NULL COMMENT '系统默认',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统导航菜单';

-- ----------------------------
-- Records of mpds_menu
-- ----------------------------

-- ----------------------------
-- Table structure for mpds_role
-- ----------------------------
DROP TABLE IF EXISTS `mpds_role`;
CREATE TABLE `mpds_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `role_type` tinyint(4) DEFAULT '2' COMMENT '1-超级用户;2- 系统管理员; 3 - 局长; 4 - 分局长; 5 - 刑警队长; 6 - 警员',
  `is_sys` tinyint(4) DEFAULT '1' COMMENT '1 - 系统管理员; 2 - 局长; 3 - 分局长; 4 - 刑警队长; 5 - 警员',
  `memo` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理角色表';

-- ----------------------------
-- Records of mpds_role
-- ----------------------------

-- ----------------------------
-- Table structure for mpds_role_value
-- ----------------------------
DROP TABLE IF EXISTS `mpds_role_value`;
CREATE TABLE `mpds_role_value` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `nav_name` varchar(100) DEFAULT NULL COMMENT '导航名称',
  `action_type` varchar(50) DEFAULT NULL COMMENT '权限类型',
  PRIMARY KEY (`id`),
  KEY `role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理角色权限表';

-- ----------------------------
-- Records of mpds_role_value
-- ----------------------------

-- ----------------------------
-- Table structure for tbApiTokens
-- ----------------------------
DROP TABLE IF EXISTS `tbApiTokens`;
CREATE TABLE `tbApiTokens` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `uCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户编号',
  `apiToken` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密钥',
  `createdTime` timestamp NULL DEFAULT NULL,
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Api访问密钥';

-- ----------------------------
-- Records of tbApiTokens
-- ----------------------------

-- ----------------------------
-- Table structure for tbAppLogs
-- ----------------------------
DROP TABLE IF EXISTS `tbAppLogs`;
CREATE TABLE `tbAppLogs` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `createdTime` timestamp NULL DEFAULT NULL,
  `fCode` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `appVersionCode` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `appApiUrl` varchar(350) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `appType` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbAppLogs
-- ----------------------------

-- ----------------------------
-- Table structure for tbAppVersions
-- ----------------------------
DROP TABLE IF EXISTS `tbAppVersions`;
CREATE TABLE `tbAppVersions` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `isDel` int(1) DEFAULT NULL,
  `recSN` blob,
  `appType` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `appVersionNo` int(11) DEFAULT NULL,
  `appVersionName` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `appPublishTime` timestamp NULL DEFAULT NULL,
  `appFeatures` longtext COLLATE utf8mb4_unicode_ci,
  `appTitle` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `appDownloadUrl` varchar(360) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createuser` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP版本管理';

-- ----------------------------
-- Records of tbAppVersions
-- ----------------------------

-- ----------------------------
-- Table structure for tbCaseCollection
-- ----------------------------
DROP TABLE IF EXISTS `tbCaseCollection`;
CREATE TABLE `tbCaseCollection` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `caseCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `createdTime` timestamp NULL DEFAULT NULL,
  `messageId` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `remark` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbCaseCollection
-- ----------------------------

-- ----------------------------
-- Table structure for tbCaseDeployDevs
-- ----------------------------
DROP TABLE IF EXISTS `tbCaseDeployDevs`;
CREATE TABLE `tbCaseDeployDevs` (
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `csCode` char(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `devCode` char(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uCode` char(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbCaseDeployDevs
-- ----------------------------

-- ----------------------------
-- Table structure for tbCaseDeploys
-- ----------------------------
DROP TABLE IF EXISTS `tbCaseDeploys`;
CREATE TABLE `tbCaseDeploys` (
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `csCode` char(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uCode` char(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rName` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbCaseDeploys
-- ----------------------------

-- ----------------------------
-- Table structure for tbCaseGoings
-- ----------------------------
DROP TABLE IF EXISTS `tbCaseGoings`;
CREATE TABLE `tbCaseGoings` (
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `csCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cgType` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cgAbstract` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cgCreateTime` timestamp NULL DEFAULT NULL,
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PositionRemark` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PositionLongitude` decimal(10,0) DEFAULT NULL,
  `PositionLatitude` decimal(10,0) DEFAULT NULL,
  `PositionName` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uUnitCode` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbCaseGoings
-- ----------------------------

-- ----------------------------
-- Table structure for tbCaseGps
-- ----------------------------
DROP TABLE IF EXISTS `tbCaseGps`;
CREATE TABLE `tbCaseGps` (
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `csCode` char(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gpsTargetType` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `devCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uCode` char(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gpsLongitude` decimal(10,0) DEFAULT NULL,
  `gpsLatitude` decimal(10,0) DEFAULT NULL,
  `gpsTime` timestamp NULL DEFAULT NULL,
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `altitude` decimal(10,0) DEFAULT NULL,
  `speed` decimal(10,0) DEFAULT NULL,
  `accelerationX` decimal(10,0) DEFAULT NULL,
  `accelerationY` decimal(10,0) DEFAULT NULL,
  `accelerationZ` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbCaseGps
-- ----------------------------

-- ----------------------------
-- Table structure for tbCaseLivingShows
-- ----------------------------
DROP TABLE IF EXISTS `tbCaseLivingShows`;
CREATE TABLE `tbCaseLivingShows` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `caseCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rtmpUrl` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `userCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `livingState` tinyint(1) DEFAULT NULL,
  `startTime` timestamp NULL DEFAULT NULL,
  `endTime` timestamp NULL DEFAULT NULL,
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `sourceType` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deviceCode` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Cumulative` int(11) DEFAULT NULL,
  `fFirstFrame` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbCaseLivingShows
-- ----------------------------

-- ----------------------------
-- Table structure for tbCaseMessages
-- ----------------------------
DROP TABLE IF EXISTS `tbCaseMessages`;
CREATE TABLE `tbCaseMessages` (
  `isDel` tinyint(1) DEFAULT NULL COMMENT '逻辑删除',
  `recSN` blob,
  `csCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `msgType` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息类型Text，Image，Audio，Video，Map, Command等',
  `msgAbstract` longtext COLLATE utf8mb4_unicode_ci COMMENT '消息内容，文字内容, 命令内容',
  `msgFile` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息相关文件id，如发送图片的文件id',
  `uCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送用户编号',
  `msgTime` datetime DEFAULT NULL COMMENT '发送时间',
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息主键',
  `uLatitude` varchar(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送人当前纬度',
  `uLongitude` varchar(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送人当前经度',
  `uPositionName` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送人当前位置',
  `msgFromType` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息来源,Group,Person,LivingRoom等',
  `msgToCode` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送给谁，讨论组则讨论组id，个人则是个人编号，直播室则是直播室id',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='IM消息';

-- ----------------------------
-- Records of tbCaseMessages
-- ----------------------------

-- ----------------------------
-- Table structure for tbCaseMission
-- ----------------------------
DROP TABLE IF EXISTS `tbCaseMission`;
CREATE TABLE `tbCaseMission` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `caseCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `createdTime` timestamp NULL DEFAULT NULL,
  `missionStatus` int(11) DEFAULT NULL,
  `missionLimiTime` int(11) DEFAULT NULL,
  `toPositionName` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `toPositionLatitude` decimal(10,0) DEFAULT NULL,
  `toPositionLongitude` decimal(10,0) DEFAULT NULL,
  `missionRemark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `routeDistance` decimal(10,0) DEFAULT NULL,
  `createdUserCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdPositionName` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdPositionLatitude` decimal(10,0) DEFAULT NULL,
  `createdPositionLongitude` decimal(10,0) DEFAULT NULL,
  `missionType` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `finishTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbCaseMission
-- ----------------------------

-- ----------------------------
-- Table structure for tbCaseMissionDistribution
-- ----------------------------
DROP TABLE IF EXISTS `tbCaseMissionDistribution`;
CREATE TABLE `tbCaseMissionDistribution` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `caseCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `createdTime` timestamp NULL DEFAULT NULL,
  `missionId` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `missionPersonStatus` int(11) DEFAULT NULL,
  `persionFinishTime` timestamp NULL DEFAULT NULL,
  `finishPositionName` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `finishPositionLatitude` decimal(10,0) DEFAULT NULL,
  `finishPositionLongitude` decimal(10,0) DEFAULT NULL,
  `userCode` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbCaseMissionDistribution
-- ----------------------------

-- ----------------------------
-- Table structure for tbCases
-- ----------------------------
DROP TABLE IF EXISTS `tbCases`;
CREATE TABLE `tbCases` (
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `csCode` char(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csName` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csType` varchar(90) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csAddress` char(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csTime` timestamp NULL DEFAULT NULL,
  `csTime_h` timestamp NULL DEFAULT NULL,
  `csStatus` char(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csDesc` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csEndFlag` char(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csEndTime` timestamp NULL DEFAULT NULL,
  `csAccept` char(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csContact` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csTel` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csPlan` longtext COLLATE utf8mb4_unicode_ci,
  `uCode` char(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csCreateTime` timestamp NULL DEFAULT NULL,
  `csRptName` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csRptSex` int(11) DEFAULT NULL,
  `csRptAge` int(11) DEFAULT NULL,
  `csRptWorkUnit` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csRptLiveAddr` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csRptTel` char(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csRptTime` timestamp NULL DEFAULT NULL,
  `csRecept` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csRptType` char(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csReceptTime` timestamp NULL DEFAULT NULL,
  `csHowDiscover` char(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csDiscoverTime` timestamp NULL DEFAULT NULL,
  `csArea` char(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csSceneType` char(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csHurtLevel` char(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csRegDate` date DEFAULT NULL,
  `csRegInst` char(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csFlag` char(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csEmail` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csReregReason` char(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csSuspCount` int(11) DEFAULT NULL,
  `csHurtCount` int(11) DEFAULT NULL,
  `csDeadCount` int(11) DEFAULT NULL,
  `csLoseVal` bigint(20) DEFAULT NULL,
  `csChoseOpp` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csChoseLoc` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csChoseObj` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csChoseArt` varchar(68) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csCrimeTrick` varchar(72) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csCrimePat` varchar(56) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csCrimeTool` varchar(68) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csEndDept` char(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csEndWay` varchar(21) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csEndType` char(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csCaptureVal` bigint(20) DEFAULT NULL,
  `csCaptCount` smallint(6) DEFAULT NULL,
  `csGrpCount` smallint(6) DEFAULT NULL,
  `csGrpKind` char(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csGrpInvCount` smallint(6) DEFAULT NULL,
  `csEndDesc` longtext COLLATE utf8mb4_unicode_ci,
  `csRescueType` char(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csRescueCount` smallint(6) DEFAULT NULL,
  `csEndChainCount` smallint(6) DEFAULT NULL,
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `csLatitude` double DEFAULT NULL,
  `csLongitude` double DEFAULT NULL,
  `csUpdateTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbCases
-- ----------------------------

-- ----------------------------
-- Table structure for tbDepartments
-- ----------------------------
DROP TABLE IF EXISTS `tbDepartments`;
CREATE TABLE `tbDepartments` (
  `isDel` int(4) DEFAULT NULL,
  `recSN` blob,
  `dCode` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dName` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dFather` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dDesc` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `sort_id` int(11) DEFAULT NULL COMMENT '排序',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `farther` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '上级部门名称',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='部门';

-- ----------------------------
-- Records of tbDepartments
-- ----------------------------

-- ----------------------------
-- Table structure for tbDeviceIMDatas
-- ----------------------------
DROP TABLE IF EXISTS `tbDeviceIMDatas`;
CREATE TABLE `tbDeviceIMDatas` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `LastModifyTime` timestamp NULL DEFAULT NULL,
  `devSn` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csCode` varchar(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `latitude` decimal(10,0) DEFAULT NULL,
  `longitude` decimal(10,0) DEFAULT NULL,
  `positionName` varchar(260) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `altitude` decimal(10,0) DEFAULT NULL,
  `speed` decimal(10,0) DEFAULT NULL,
  `accelerationX` decimal(10,0) DEFAULT NULL,
  `accelerationY` decimal(10,0) DEFAULT NULL,
  `accelerationZ` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbDeviceIMDatas
-- ----------------------------

-- ----------------------------
-- Table structure for tbDevices
-- ----------------------------
DROP TABLE IF EXISTS `tbDevices`;
CREATE TABLE `tbDevices` (
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `devCode` char(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `devName` char(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `devType` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `devBrand` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `devModel` char(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `devPDate` date DEFAULT NULL,
  `devGPeriod` int(11) DEFAULT NULL,
  `devSTime` date DEFAULT NULL,
  `uCode` char(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `devStatus` char(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `devPhoto` char(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `devRemark` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `devSN` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbDevices
-- ----------------------------

-- ----------------------------
-- Table structure for tbDiscussionGroupMenbers
-- ----------------------------
DROP TABLE IF EXISTS `tbDiscussionGroupMenbers`;
CREATE TABLE `tbDiscussionGroupMenbers` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `createdTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `discussionCode` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '讨论组编号',
  `createdUserCode` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人编号',
  `createdUserName` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人姓名',
  `uCode` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '讨论组成员编号',
  `csCode` varchar(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='通话组成员';

-- ----------------------------
-- Records of tbDiscussionGroupMenbers
-- ----------------------------

-- ----------------------------
-- Table structure for tbDiscussionGroups
-- ----------------------------
DROP TABLE IF EXISTS `tbDiscussionGroups`;
CREATE TABLE `tbDiscussionGroups` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `isDel` tinyint(1) DEFAULT NULL COMMENT '逻辑删除',
  `recSN` blob,
  `createdTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `discussionCode` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '讨论组编号',
  `discussionName` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '讨论组名字',
  `createdUserCode` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `createdUserName` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建时间',
  `csCode` varchar(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` char(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'PDT；APP',
  `relativegroup` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '融合组名，只对类型为APP的组别有效、且只能是PDT组',
  `relativegroupid` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '融合组ID，只对类型为APP的组别有效、且只能是PDT组',
  `dept` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门',
  `deptid` int(11) DEFAULT NULL COMMENT '部门ID',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '0 - 启用； 1 - 禁用 与isDel取值一致',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='通话组';

-- ----------------------------
-- Records of tbDiscussionGroups
-- ----------------------------
INSERT INTO `tbDiscussionGroups` VALUES ('1', '0', null, '2018-09-01 12:15:47', '10009', '10009', 'admin', 'admin', null, 'APP', '10010', '10010', null, null, null, null);

-- ----------------------------
-- Table structure for tbFileDetails
-- ----------------------------
DROP TABLE IF EXISTS `tbFileDetails`;
CREATE TABLE `tbFileDetails` (
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `attRsCode` char(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fCode` char(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件编号',
  `fName` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件名字',
  `fSize` bigint(20) DEFAULT NULL COMMENT '文件大小',
  `fAbstract` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '内容描述',
  `fFirstFrame` char(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '视频的第一帧图片',
  `fStartTime` timestamp NULL DEFAULT NULL COMMENT '语音开始时间',
  `fEndTime` timestamp NULL DEFAULT NULL COMMENT '语音结束时间',
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fRelativePath` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件存放地址',
  `fduration` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `virtualId` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '虚拟id，比如关联讨论组，可以根据id查所有讨论组或者其他的的文件',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='消息内容相关文件';

-- ----------------------------
-- Records of tbFileDetails
-- ----------------------------

-- ----------------------------
-- Table structure for tbRoles
-- ----------------------------
DROP TABLE IF EXISTS `tbRoles`;
CREATE TABLE `tbRoles` (
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `rName` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rDesc` char(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uCode` char(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rEditTime` timestamp NULL DEFAULT NULL,
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbRoles
-- ----------------------------

-- ----------------------------
-- Table structure for tbUnits
-- ----------------------------
DROP TABLE IF EXISTS `tbUnits`;
CREATE TABLE `tbUnits` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `unitCode` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `maxUserNumber` int(11) DEFAULT NULL,
  `maxVideoUserNumber` int(11) DEFAULT NULL,
  `unitName` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `unitLoginName` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uHeadPortrait` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PIC` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Duty` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Tel` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Province` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `City` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `County` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `detailAddress` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Remarks` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbUnits
-- ----------------------------

-- ----------------------------
-- Table structure for tbUserIMDatas
-- ----------------------------
DROP TABLE IF EXISTS `tbUserIMDatas`;
CREATE TABLE `tbUserIMDatas` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `LastModifyTime` timestamp NULL DEFAULT NULL,
  `uCode` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csCode` varchar(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `latitude` double(10,5) DEFAULT NULL,
  `longitude` double(10,5) DEFAULT NULL,
  `positionName` varchar(260) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `altitude` decimal(10,0) DEFAULT NULL,
  `speed` decimal(10,0) DEFAULT NULL,
  `accelerationX` decimal(10,0) DEFAULT NULL,
  `accelerationY` decimal(10,0) DEFAULT NULL,
  `accelerationZ` decimal(10,0) DEFAULT NULL,
  `type` char(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'PDT；APP',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户坐标';

-- ----------------------------
-- Records of tbUserIMDatas
-- ----------------------------

-- ----------------------------
-- Table structure for tbusers
-- ----------------------------
DROP TABLE IF EXISTS `tbUsers`;
CREATE TABLE `tbUsers` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `isDel` int(4) DEFAULT NULL COMMENT '逻辑删除',
  `recSN` blob,
  `uCode` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户编号',
  `uPassword` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `uSalt` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uBelong` char(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `uIsActive` int(4) DEFAULT NULL COMMENT '是否激活',
  `rName` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pcNum` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '警员编号',
  `uName` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  `uSex` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `uDuty` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '职位',
  `dCode` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门编号，关联mpds_dept表',
  `uTel` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uShortNum` char(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话号码',
  `uHeadPortrait` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `dName` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门名称',
  `LYCID` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `loginFailTimes` smallint(6) DEFAULT NULL,
  `lastLoginTime` datetime DEFAULT NULL,
  `uRemarks` longtext COLLATE utf8mb4_unicode_ci,
  `Createtime` datetime DEFAULT NULL,
  `uDepartment` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `accountType` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号类型，警种：1、治安警察；2、刑事警察；3、交通警察；4、巡逻警察；5、特种警察；6、武装警察；7、空中警察；8、中国海警',
  `uEmployeenum` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uIshistory` tinyint(1) DEFAULT NULL,
  `uIsUnilt` tinyint(1) DEFAULT NULL,
  `uIsAccontion` tinyint(1) DEFAULT NULL,
  `uUnitCode` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '单位编号=部门编号',
  `roleid` int(11) NOT NULL COMMENT '角色编号，关联mpds_role',
  `roleType` int(11) NOT NULL COMMENT '1、治安警察；2、刑事警察；3、交通警察；4、巡逻警察；5、特种警察；6、武装警察；7、空中警察；8、中国海警',
  `groupid` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '当前组ID',
  `groupName` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '当前组名',
  `deviceid` int(11) DEFAULT NULL COMMENT '移动台号',
  `deviceESN` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '移动台ESN',
  `devicetype` char(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'PDT；APP',
  `status` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '0、离线；1、在线、2、遥晕；3、摇毙；4、禁用',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户信息';

-- ----------------------------
-- Records of tbusers
-- ----------------------------
INSERT INTO `tbUsers` VALUES ('1', '0', null, 'admin', '87FA6AD6CBFDF3108E4DD6F47F5D04A4', '24V0XZ', '', null, '超级管理员', null, '超级管理员', '男', '警监', 'S0002', '13789091234', '44400', '', '天河区公安局', null, null, null, null, '2018-09-04 08:33:19', '天河区公安局', '其他', null, null, null, null, '', '1', '1', 'F009', '专案组', null, null, null, '离线');
INSERT INTO `tbUsers` VALUES ('7', '0', null, 'darren', '91FC8F5D0BC7B906', '884424', '', '0', '前台用户', null, '李小明', '男', '警员', 'S0005', '13809782354', '334674', '', '海珠区公安局', null, null, null, null, '2018-09-03 15:01:10', '海珠区公安局', '特种警察', null, null, null, null, '', '3', '2', 'F009', '专案组', null, null, null, '离线');

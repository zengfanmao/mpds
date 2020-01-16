/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : dbchat

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 23/08/2018 09:23:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbapitokens
-- ----------------------------
DROP TABLE IF EXISTS `tbapitokens`;
CREATE TABLE `tbapitokens`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `uCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `apiToken` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `createdTime` timestamp(0) NULL DEFAULT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbapitokens
-- ----------------------------
INSERT INTO `tbapitokens` VALUES ('2be4f64b-ff7e-4304-bada-acfb0c8670fc', '1001', 'T8WTbiKlVcADow43', '2018-08-22 16:11:53', 0, 0xD58B48EB0608D688);
INSERT INTO `tbapitokens` VALUES ('ac0c1c93-5093-4a6e-8f40-5f3ae42a14fd', '1009', 'g3tRG4CaiHPCJMbX', '2018-08-22 16:12:30', 0, 0xA85D68010708D688);

-- ----------------------------
-- Table structure for tbapplogs
-- ----------------------------
DROP TABLE IF EXISTS `tbapplogs`;
CREATE TABLE `tbapplogs`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `createdTime` timestamp(0) NULL DEFAULT NULL,
  `fCode` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `appVersionCode` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `appApiUrl` varchar(350) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `appType` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbappversions
-- ----------------------------
DROP TABLE IF EXISTS `tbappversions`;
CREATE TABLE `tbappversions`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `appType` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `appVersionNo` int(11) NULL DEFAULT NULL,
  `appVersionName` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `appPublishTime` timestamp(0) NULL DEFAULT NULL,
  `appFeatures` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `appTitle` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `appDownloadUrl` varchar(360) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcasecollection
-- ----------------------------
DROP TABLE IF EXISTS `tbcasecollection`;
CREATE TABLE `tbcasecollection`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caseCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `createdTime` timestamp(0) NULL DEFAULT NULL,
  `messageId` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbcasecollection
-- ----------------------------
INSERT INTO `tbcasecollection` VALUES ('BA71CB75-D17D-443D-A7A3-ED42C71E9586', '123                     ', 0, 0x0000000000000C07, '2018-06-25 18:07:08', 'cb9c490b-f9fe-497c-8906-6154d4b2d03c', NULL, 'wang');

-- ----------------------------
-- Table structure for tbcasedeploydevs
-- ----------------------------
DROP TABLE IF EXISTS `tbcasedeploydevs`;
CREATE TABLE `tbcasedeploydevs`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `csCode` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `devCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcasedeploys
-- ----------------------------
DROP TABLE IF EXISTS `tbcasedeploys`;
CREATE TABLE `tbcasedeploys`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `csCode` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uCode` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `rName` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbcasedeploys
-- ----------------------------
INSERT INTO `tbcasedeploys` VALUES (0, 0x000000000000C614, '1', '1001', NULL, '522B4E0E-34A0-E811-96FD-509A4C2006E1');
INSERT INTO `tbcasedeploys` VALUES (0, 0x000000000000C615, '1', '1002', NULL, '532B4E0E-34A0-E811-96FD-509A4C2006E1');
INSERT INTO `tbcasedeploys` VALUES (0, 0x000000000000C616, '1', '1003', NULL, '542B4E0E-34A0-E811-96FD-509A4C2006E1');
INSERT INTO `tbcasedeploys` VALUES (0, 0x0000000000000D17, '123', 'libai', NULL, 'E7B9988F-8B77-E811-96F8-509A1C1202E1');
INSERT INTO `tbcasedeploys` VALUES (0, 0x00000000000007FE, '123', 'baiyuanwei', NULL, 'E7B9988F-8B77-E811-96F8-509A1C2001E1');
INSERT INTO `tbcasedeploys` VALUES (0, 0x0000000000000D18, '123', 'dufu', NULL, 'E7B9988F-8B77-E811-96F8-509A1C2006E1');
INSERT INTO `tbcasedeploys` VALUES (0, 0x00000000000007DA, '123', 'hexiang', NULL, 'E7B9988F-8B77-E811-96F8-509A4C2006E1');
INSERT INTO `tbcasedeploys` VALUES (0, 0x0000000000004C8E, '123', 'meixi', NULL, 'E7B9988F-8B77-E811-96F8-509A4C3006E1');

-- ----------------------------
-- Table structure for tbcasegoings
-- ----------------------------
DROP TABLE IF EXISTS `tbcasegoings`;
CREATE TABLE `tbcasegoings`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `csCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `cgType` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `cgAbstract` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `cgCreateTime` timestamp(0) NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `PositionRemark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `PositionLongitude` decimal(10, 0) NULL DEFAULT NULL,
  `PositionLatitude` decimal(10, 0) NULL DEFAULT NULL,
  `PositionName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uUnitCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcasegps
-- ----------------------------
DROP TABLE IF EXISTS `tbcasegps`;
CREATE TABLE `tbcasegps`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `csCode` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `gpsTargetType` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `devCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `gpsLongitude` decimal(10, 0) NULL DEFAULT NULL,
  `gpsLatitude` decimal(10, 0) NULL DEFAULT NULL,
  `gpsTime` timestamp(0) NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `altitude` decimal(10, 0) NULL DEFAULT NULL,
  `speed` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationX` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationY` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationZ` decimal(10, 0) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcaselivingshows
-- ----------------------------
DROP TABLE IF EXISTS `tbcaselivingshows`;
CREATE TABLE `tbcaselivingshows`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caseCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `rtmpUrl` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `userCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `livingState` tinyint(1) NULL DEFAULT NULL,
  `startTime` timestamp(0) NULL DEFAULT NULL,
  `endTime` timestamp(0) NULL DEFAULT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `sourceType` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deviceCode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `Cumulative` int(11) NULL DEFAULT NULL,
  `fFirstFrame` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcasemessages
-- ----------------------------
DROP TABLE IF EXISTS `tbcasemessages`;
CREATE TABLE `tbcasemessages`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `csCode` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `msgType` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `msgAbstract` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `msgFile` char(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `msgTime` timestamp(0) NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `uLatitude` decimal(10, 0) NULL DEFAULT NULL,
  `uLongitude` decimal(10, 0) NULL DEFAULT NULL,
  `uPositionName` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `msgFromType` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `msgToCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcasemission
-- ----------------------------
DROP TABLE IF EXISTS `tbcasemission`;
CREATE TABLE `tbcasemission`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caseCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `createdTime` timestamp(0) NULL DEFAULT NULL,
  `missionStatus` int(11) NULL DEFAULT NULL,
  `missionLimiTime` int(11) NULL DEFAULT NULL,
  `toPositionName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `toPositionLatitude` decimal(10, 0) NULL DEFAULT NULL,
  `toPositionLongitude` decimal(10, 0) NULL DEFAULT NULL,
  `missionRemark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `routeDistance` decimal(10, 0) NULL DEFAULT NULL,
  `createdUserCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `createdPositionName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `createdPositionLatitude` decimal(10, 0) NULL DEFAULT NULL,
  `createdPositionLongitude` decimal(10, 0) NULL DEFAULT NULL,
  `missionType` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `finishTime` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcasemissiondistribution
-- ----------------------------
DROP TABLE IF EXISTS `tbcasemissiondistribution`;
CREATE TABLE `tbcasemissiondistribution`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caseCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `createdTime` timestamp(0) NULL DEFAULT NULL,
  `missionId` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `missionPersonStatus` int(11) NULL DEFAULT NULL,
  `persionFinishTime` timestamp(0) NULL DEFAULT NULL,
  `finishPositionName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `finishPositionLatitude` decimal(10, 0) NULL DEFAULT NULL,
  `finishPositionLongitude` decimal(10, 0) NULL DEFAULT NULL,
  `userCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcases
-- ----------------------------
DROP TABLE IF EXISTS `tbcases`;
CREATE TABLE `tbcases`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `csCode` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csName` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csType` varchar(90) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csAddress` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csTime` timestamp(0) NULL DEFAULT NULL,
  `csTime_h` timestamp(0) NULL DEFAULT NULL,
  `csStatus` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csDesc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csEndFlag` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csEndTime` timestamp(0) NULL DEFAULT NULL,
  `csAccept` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csContact` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csTel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csPlan` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `uCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csCreateTime` timestamp(0) NULL DEFAULT NULL,
  `csRptName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csRptSex` int(11) NULL DEFAULT NULL,
  `csRptAge` int(11) NULL DEFAULT NULL,
  `csRptWorkUnit` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csRptLiveAddr` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csRptTel` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csRptTime` timestamp(0) NULL DEFAULT NULL,
  `csRecept` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csRptType` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csReceptTime` timestamp(0) NULL DEFAULT NULL,
  `csHowDiscover` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csDiscoverTime` timestamp(0) NULL DEFAULT NULL,
  `csArea` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csSceneType` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csHurtLevel` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csRegDate` date NULL DEFAULT NULL,
  `csRegInst` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csFlag` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csEmail` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csReregReason` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csSuspCount` int(11) NULL DEFAULT NULL,
  `csHurtCount` int(11) NULL DEFAULT NULL,
  `csDeadCount` int(11) NULL DEFAULT NULL,
  `csLoseVal` bigint(20) NULL DEFAULT NULL,
  `csChoseOpp` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csChoseLoc` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csChoseObj` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csChoseArt` varchar(68) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csCrimeTrick` varchar(72) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csCrimePat` varchar(56) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csCrimeTool` varchar(68) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csEndDept` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csEndWay` varchar(21) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csEndType` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csCaptureVal` bigint(20) NULL DEFAULT NULL,
  `csCaptCount` smallint(6) NULL DEFAULT NULL,
  `csGrpCount` smallint(6) NULL DEFAULT NULL,
  `csGrpKind` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csGrpInvCount` smallint(6) NULL DEFAULT NULL,
  `csEndDesc` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `csRescueType` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csRescueCount` smallint(6) NULL DEFAULT NULL,
  `csEndChainCount` smallint(6) NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `csLatitude` double NULL DEFAULT NULL,
  `csLongitude` double NULL DEFAULT NULL,
  `csUpdateTime` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbdepartments
-- ----------------------------
DROP TABLE IF EXISTS `tbdepartments`;
CREATE TABLE `tbdepartments`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `dCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `dName` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `dFather` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `dDesc` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbdepartments
-- ----------------------------
INSERT INTO `tbdepartments` VALUES (0, 0x0000000000000925, '123456', '研发部门', NULL, NULL, '9377-E811-96F8-509A4C2006E1');

-- ----------------------------
-- Table structure for tbdeviceimdatas
-- ----------------------------
DROP TABLE IF EXISTS `tbdeviceimdatas`;
CREATE TABLE `tbdeviceimdatas`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `LastModifyTime` timestamp(0) NULL DEFAULT NULL,
  `devSn` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csCode` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `latitude` decimal(10, 0) NULL DEFAULT NULL,
  `longitude` decimal(10, 0) NULL DEFAULT NULL,
  `positionName` varchar(260) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `altitude` decimal(10, 0) NULL DEFAULT NULL,
  `speed` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationX` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationY` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationZ` decimal(10, 0) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbdevices
-- ----------------------------
DROP TABLE IF EXISTS `tbdevices`;
CREATE TABLE `tbdevices`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `devCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `devName` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `devType` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `devBrand` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `devModel` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `devPDate` date NULL DEFAULT NULL,
  `devGPeriod` int(11) NULL DEFAULT NULL,
  `devSTime` date NULL DEFAULT NULL,
  `uCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `devStatus` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `devPhoto` char(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `devRemark` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `devSN` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbdiscussiongroupmenbers
-- ----------------------------
DROP TABLE IF EXISTS `tbdiscussiongroupmenbers`;
CREATE TABLE `tbdiscussiongroupmenbers`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `createdTime` timestamp(0) NULL DEFAULT NULL,
  `discussionCode` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `createdUserCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `createdUserName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uCode` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csCode` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbdiscussiongroupmenbers
-- ----------------------------
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('620372E7-96A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E2A3, '2018-08-15 22:24:23', '2', 'hexiang', NULL, '1001', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('630372E7-96A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E2A4, '2018-08-15 22:24:23', '2', 'hexiang', NULL, '1002', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('640372E7-96A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E2A5, '2018-08-15 22:24:23', '2', 'hexiang', NULL, '1003', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('650372E7-96A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E2A6, '2018-08-15 22:24:23', '2', 'hexiang', NULL, '1004', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('660372E7-96A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E2A7, '2018-08-15 22:24:23', '2', 'hexiang', NULL, '1005', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('670372E7-96A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E2A8, '2018-08-15 22:24:23', '2', 'hexiang', NULL, '1006', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('680372E7-96A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E2A9, '2018-08-15 22:24:23', '2', 'hexiang', NULL, '1007', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('690372E7-96A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E2AA, '2018-08-15 22:24:23', '2', 'hexiang', NULL, '1008', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('690372E7-96A0-E811-96FE-509A4C2006E2', 0, 0x000000000001406B, '2018-08-15 22:24:23', '3', 'hexiang', NULL, '1009', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('690372E7-96A0-E811-96FE-509A4C2006E3', 0, 0x000000000001406C, '2018-08-15 22:24:23', '3', 'hexiang', NULL, '1010', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('80E7E692-95A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E29A, '2018-08-15 22:14:52', '1', 'hexiang', NULL, '1001', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('81E7E692-95A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E29B, '2018-08-15 22:14:52', '1', 'hexiang', NULL, '1002', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('82E7E692-95A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E29C, '2018-08-15 22:14:52', '1', 'hexiang', NULL, '1003', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('83E7E692-95A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E29D, '2018-08-15 22:14:52', '1', 'hexiang', NULL, '1004', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('84E7E692-95A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E29E, '2018-08-15 22:14:52', '1', 'hexiang', NULL, '1005', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('85E7E692-95A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E29F, '2018-08-15 22:14:52', '1', 'hexiang', NULL, '1006', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('86E7E692-95A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E2A0, '2018-08-15 22:14:52', '1', 'hexiang', NULL, '1007', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('87E7E692-95A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E2A1, '2018-08-15 22:14:52', '1', 'hexiang', NULL, '1008', '');

-- ----------------------------
-- Table structure for tbdiscussiongroups
-- ----------------------------
DROP TABLE IF EXISTS `tbdiscussiongroups`;
CREATE TABLE `tbdiscussiongroups`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NOT NULL,
  `createdTime` timestamp(0) NULL DEFAULT NULL,
  `discussionCode` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `discussionName` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `createdUserCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `createdUserName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csCode` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbdiscussiongroups
-- ----------------------------
INSERT INTO `tbdiscussiongroups` VALUES ('5F4A7B23-95A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E298, '2018-08-15 22:11:45', '1', '交流大会', 'hexiang', NULL, '');
INSERT INTO `tbdiscussiongroups` VALUES ('610372E7-96A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E2A2, '2018-08-15 22:24:23', '2', '保护首长', 'hexiang', NULL, '');
INSERT INTO `tbdiscussiongroups` VALUES ('610372E7-96A0-E811-96FE-509A4C2006E3', 0, 0x0000000000014066, '2018-08-15 22:24:23', '3', '开发测试大会', 'hexiang', NULL, '');

-- ----------------------------
-- Table structure for tbfiledetails
-- ----------------------------
DROP TABLE IF EXISTS `tbfiledetails`;
CREATE TABLE `tbfiledetails`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `attRsCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `fCode` char(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `fName` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `fSize` bigint(20) NULL DEFAULT NULL,
  `fAbstract` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `fFirstFrame` char(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `fStartTime` timestamp(0) NULL DEFAULT NULL,
  `fEndTime` timestamp(0) NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `fRelativePath` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `virtualId` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbroles
-- ----------------------------
DROP TABLE IF EXISTS `tbroles`;
CREATE TABLE `tbroles`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `rName` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `rDesc` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `rEditTime` timestamp(0) NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbunits
-- ----------------------------
DROP TABLE IF EXISTS `tbunits`;
CREATE TABLE `tbunits`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `unitCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `maxUserNumber` int(11) NULL DEFAULT NULL,
  `maxVideoUserNumber` int(11) NULL DEFAULT NULL,
  `unitName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `unitLoginName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uHeadPortrait` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `PIC` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `Duty` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `Tel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `Province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `City` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `County` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `detailAddress` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `Remarks` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbunits
-- ----------------------------
INSERT INTO `tbunits` VALUES ('A0BDD879-DE76-E811-96F8-109A4C2006E3', '321', 1111, 111, 'xxx单位测试组', 'xxx单位', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tbunits` VALUES ('F064F30D-4576-4472-A1CD-CA7D83395D5D', 'A61DEBB3-DF77-4731-942B-0187C931E489', 100, 100, '交流演示', '1001', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for tbuserimdatas
-- ----------------------------
DROP TABLE IF EXISTS `tbuserimdatas`;
CREATE TABLE `tbuserimdatas`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `LastModifyTime` timestamp(0) NULL DEFAULT NULL,
  `uCode` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `csCode` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `latitude` decimal(10, 0) NULL DEFAULT NULL,
  `longitude` decimal(10, 0) NULL DEFAULT NULL,
  `positionName` varchar(260) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `altitude` decimal(10, 0) NULL DEFAULT NULL,
  `speed` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationX` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationY` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationZ` decimal(10, 0) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbusers
-- ----------------------------
DROP TABLE IF EXISTS `tbusers`;
CREATE TABLE `tbusers`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `uCode` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uBelong` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uIsActive` tinyint(1) NULL DEFAULT NULL,
  `rName` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `pcNum` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uSex` int(11) NULL DEFAULT NULL,
  `uDuty` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `dCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uTel` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uShortNum` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uPassword` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `uHeadPortrait` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `dName` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `LYCID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `loginFailTimes` smallint(6) NULL DEFAULT NULL,
  `lastLoginTime` timestamp(0) NULL DEFAULT NULL,
  `uRemarks` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `Createtime` timestamp(0) NULL DEFAULT NULL,
  `uDepartment` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `accountType` smallint(6) NULL DEFAULT NULL,
  `uEmployeenum` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `uIshistory` tinyint(1) NULL DEFAULT NULL,
  `uIsUnilt` tinyint(1) NULL DEFAULT NULL,
  `uIsAccontion` tinyint(1) NULL DEFAULT NULL,
  `uUnitCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbusers
-- ----------------------------
INSERT INTO `tbusers` VALUES (0, 0x0000000000010290, '1001', NULL, 1, NULL, NULL, '陆警官', 0, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489', NULL, NULL, '06E6C0EA3096DA10229E4B8D9612BAC3', '4A2B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2986382168,4102092451&fm=27&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES (0, 0x0000000000010291, '1002', NULL, 1, NULL, NULL, '王警官', 0, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489', NULL, NULL, '06E6C0EA3096DA10229E4B8D9612BAC3', '4B2B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1070143562,438881316&fm=27&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES (0, 0x0000000000010292, '1003', NULL, 1, NULL, NULL, '李sir', 0, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489', NULL, NULL, '06E6C0EA3096DA10229E4B8D9612BAC3', '4C2B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=340995978,612007333&fm=27&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES (0, 0x0000000000010293, '1004', NULL, 1, NULL, NULL, '白sir', 0, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489', NULL, NULL, '06E6C0EA3096DA10229E4B8D9612BAC3', '4D2B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2634757784,2366771600&fm=27&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES (0, 0x0000000000010294, '1005', NULL, 1, NULL, NULL, '贺sir', 0, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489', NULL, NULL, '06E6C0EA3096DA10229E4B8D9612BAC3', '4E2B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2783059890,2859384148&fm=27&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES (0, 0x0000000000010295, '1006', NULL, 1, NULL, NULL, '赵sir', 0, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489', NULL, NULL, '06E6C0EA3096DA10229E4B8D9612BAC3', '4F2B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3994279899,2192014873&fm=200&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES (0, 0x0000000000010296, '1007', NULL, 1, NULL, NULL, '谢sir', 0, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489', NULL, NULL, '06E6C0EA3096DA10229E4B8D9612BAC3', '502B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1347383990,412756325&fm=27&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES (0, 0x0000000000010297, '1008', NULL, 1, NULL, NULL, '叶sir', 0, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489', NULL, NULL, '06E6C0EA3096DA10229E4B8D9612BAC3', '512B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1178026059,2522313002&fm=27&gp=0.jpgg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES (0, 0x0000000000014061, '1009', NULL, 1, NULL, NULL, '开发专用1号', 0, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489', NULL, NULL, '06E6C0EA3096DA10229E4B8D9612BAC3', '512B4E0E-34A0-E811-96FD-509A4C2006E2', 'http://img5.imgtn.bdimg.com/it/u=2832797993,2676010642&fm=27&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES (0, 0x0000000000014070, '1010', NULL, 1, NULL, NULL, '开发专用2号', 0, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489', NULL, NULL, '06E6C0EA3096DA10229E4B8D9612BAC3', '512B4E0E-34A0-E811-96FD-509A4C2006E3', 'http://img1.imgtn.bdimg.com/it/u=3402051892,3248047759&fm=27&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES (0, 0x000000000001028E, 'baiyuanwei', NULL, 1, NULL, NULL, '白垣伟', 0, '武警', '123456', '4444', NULL, 'E10ADC3949BA59ABBE56E057F20F883E', '79CDCF99-8C77-E811-96F8-509A4C2006E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2986382168,4102092451&fm=27&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, '321');
INSERT INTO `tbusers` VALUES (0, 0x000000000001028A, 'hexiang', NULL, 1, NULL, NULL, '贺祥', 0, '警察', '123456', NULL, NULL, 'E10ADC3949BA59ABBE56E057F20F883E', 'A0BDD879-DE76-E811-96F8-109A4C2006E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2986382168,4102092451&fm=27&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, '321');
INSERT INTO `tbusers` VALUES (0, 0x000000000001028B, 'meixi', NULL, 1, NULL, NULL, '梅西', 0, '警察', '123456', NULL, NULL, 'E10ADC3949BA59ABBE56E057F20F883E', 'A0BDD879-DE76-E811-96F8-109A4C2106E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2986382168,4102092451&fm=27&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, '321');
INSERT INTO `tbusers` VALUES (0, 0x000000000001028C, 'dufu', NULL, 1, NULL, NULL, '杜甫', 0, '警察', '123456', NULL, NULL, 'E10ADC3949BA59ABBE56E057F20F883E', 'A0BDD879-DE76-E811-96F8-109A4C3006E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2986382168,4102092451&fm=27&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, '321');
INSERT INTO `tbusers` VALUES (0, 0x000000000001028D, 'libai', NULL, 1, NULL, NULL, '李白', 0, '警察', '123456', NULL, NULL, 'E10ADC3949BA59ABBE56E057F20F883E', 'A0BDD879-DE76-E811-96F8-509A4C2006E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2986382168,4102092451&fm=27&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, '321');

SET FOREIGN_KEY_CHECKS = 1;

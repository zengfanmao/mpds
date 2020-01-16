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

 Date: 28/08/2018 09:47:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbapplogs
-- ----------------------------
DROP TABLE IF EXISTS `tbapplogs`;
CREATE TABLE `tbapplogs`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `createdTime` timestamp(0) NULL DEFAULT NULL,
  `fCode` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `appVersionCode` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `appApiUrl` varchar(350) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `appType` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbappversions
-- ----------------------------
DROP TABLE IF EXISTS `tbappversions`;
CREATE TABLE `tbappversions`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `appType` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `appVersionNo` int(11) NULL DEFAULT NULL,
  `appVersionName` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `appPublishTime` timestamp(0) NULL DEFAULT NULL,
  `appFeatures` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `appTitle` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `appDownloadUrl` varchar(360) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcasecollection
-- ----------------------------
DROP TABLE IF EXISTS `tbcasecollection`;
CREATE TABLE `tbcasecollection`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `caseCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `createdTime` timestamp(0) NULL DEFAULT NULL,
  `messageId` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

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
  `csCode` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `devCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcasedeploys
-- ----------------------------
DROP TABLE IF EXISTS `tbcasedeploys`;
CREATE TABLE `tbcasedeploys`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `csCode` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uCode` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `rName` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

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
  `csCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cgType` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cgAbstract` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cgCreateTime` timestamp(0) NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `PositionRemark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `PositionLongitude` decimal(10, 0) NULL DEFAULT NULL,
  `PositionLatitude` decimal(10, 0) NULL DEFAULT NULL,
  `PositionName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uUnitCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcasegps
-- ----------------------------
DROP TABLE IF EXISTS `tbcasegps`;
CREATE TABLE `tbcasegps`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `csCode` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gpsTargetType` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `devCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gpsLongitude` decimal(10, 0) NULL DEFAULT NULL,
  `gpsLatitude` decimal(10, 0) NULL DEFAULT NULL,
  `gpsTime` timestamp(0) NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `altitude` decimal(10, 0) NULL DEFAULT NULL,
  `speed` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationX` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationY` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationZ` decimal(10, 0) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcaselivingshows
-- ----------------------------
DROP TABLE IF EXISTS `tbcaselivingshows`;
CREATE TABLE `tbcaselivingshows`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `caseCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `rtmpUrl` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `userCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `livingState` tinyint(1) NULL DEFAULT NULL,
  `startTime` timestamp(0) NULL DEFAULT NULL,
  `endTime` timestamp(0) NULL DEFAULT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `sourceType` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deviceCode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `Cumulative` int(11) NULL DEFAULT NULL,
  `fFirstFrame` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcasemission
-- ----------------------------
DROP TABLE IF EXISTS `tbcasemission`;
CREATE TABLE `tbcasemission`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `caseCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `createdTime` timestamp(0) NULL DEFAULT NULL,
  `missionStatus` int(11) NULL DEFAULT NULL,
  `missionLimiTime` int(11) NULL DEFAULT NULL,
  `toPositionName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `toPositionLatitude` decimal(10, 0) NULL DEFAULT NULL,
  `toPositionLongitude` decimal(10, 0) NULL DEFAULT NULL,
  `missionRemark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `routeDistance` decimal(10, 0) NULL DEFAULT NULL,
  `createdUserCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `createdPositionName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `createdPositionLatitude` decimal(10, 0) NULL DEFAULT NULL,
  `createdPositionLongitude` decimal(10, 0) NULL DEFAULT NULL,
  `missionType` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `finishTime` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcasemissiondistribution
-- ----------------------------
DROP TABLE IF EXISTS `tbcasemissiondistribution`;
CREATE TABLE `tbcasemissiondistribution`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `caseCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `createdTime` timestamp(0) NULL DEFAULT NULL,
  `missionId` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `missionPersonStatus` int(11) NULL DEFAULT NULL,
  `persionFinishTime` timestamp(0) NULL DEFAULT NULL,
  `finishPositionName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `finishPositionLatitude` decimal(10, 0) NULL DEFAULT NULL,
  `finishPositionLongitude` decimal(10, 0) NULL DEFAULT NULL,
  `userCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbcases
-- ----------------------------
DROP TABLE IF EXISTS `tbcases`;
CREATE TABLE `tbcases`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `csCode` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csName` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csType` varchar(90) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csAddress` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csTime` timestamp(0) NULL DEFAULT NULL,
  `csTime_h` timestamp(0) NULL DEFAULT NULL,
  `csStatus` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csDesc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csEndFlag` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csEndTime` timestamp(0) NULL DEFAULT NULL,
  `csAccept` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csContact` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csTel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csPlan` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `uCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csCreateTime` timestamp(0) NULL DEFAULT NULL,
  `csRptName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csRptSex` int(11) NULL DEFAULT NULL,
  `csRptAge` int(11) NULL DEFAULT NULL,
  `csRptWorkUnit` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csRptLiveAddr` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csRptTel` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csRptTime` timestamp(0) NULL DEFAULT NULL,
  `csRecept` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csRptType` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csReceptTime` timestamp(0) NULL DEFAULT NULL,
  `csHowDiscover` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csDiscoverTime` timestamp(0) NULL DEFAULT NULL,
  `csArea` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csSceneType` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csHurtLevel` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csRegDate` date NULL DEFAULT NULL,
  `csRegInst` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csFlag` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csEmail` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csReregReason` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csSuspCount` int(11) NULL DEFAULT NULL,
  `csHurtCount` int(11) NULL DEFAULT NULL,
  `csDeadCount` int(11) NULL DEFAULT NULL,
  `csLoseVal` bigint(20) NULL DEFAULT NULL,
  `csChoseOpp` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csChoseLoc` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csChoseObj` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csChoseArt` varchar(68) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csCrimeTrick` varchar(72) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csCrimePat` varchar(56) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csCrimeTool` varchar(68) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csEndDept` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csEndWay` varchar(21) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csEndType` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csCaptureVal` bigint(20) NULL DEFAULT NULL,
  `csCaptCount` smallint(6) NULL DEFAULT NULL,
  `csGrpCount` smallint(6) NULL DEFAULT NULL,
  `csGrpKind` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csGrpInvCount` smallint(6) NULL DEFAULT NULL,
  `csEndDesc` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `csRescueType` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csRescueCount` smallint(6) NULL DEFAULT NULL,
  `csEndChainCount` smallint(6) NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `csLatitude` double NULL DEFAULT NULL,
  `csLongitude` double NULL DEFAULT NULL,
  `csUpdateTime` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbdepartments
-- ----------------------------
DROP TABLE IF EXISTS `tbdepartments`;
CREATE TABLE `tbdepartments`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `dCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dName` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dFather` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dDesc` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbdepartments
-- ----------------------------
INSERT INTO `tbdepartments` VALUES (0, 0x0000000000000925, '123456', '研发部门', NULL, NULL, '9377-E811-96F8-509A4C2006E1');

-- ----------------------------
-- Table structure for tbdeviceimdatas
-- ----------------------------
DROP TABLE IF EXISTS `tbdeviceimdatas`;
CREATE TABLE `tbdeviceimdatas`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `LastModifyTime` timestamp(0) NULL DEFAULT NULL,
  `devSn` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csCode` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `latitude` decimal(10, 0) NULL DEFAULT NULL,
  `longitude` decimal(10, 0) NULL DEFAULT NULL,
  `positionName` varchar(260) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `altitude` decimal(10, 0) NULL DEFAULT NULL,
  `speed` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationX` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationY` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationZ` decimal(10, 0) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbdevices
-- ----------------------------
DROP TABLE IF EXISTS `tbdevices`;
CREATE TABLE `tbdevices`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `devCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `devName` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `devType` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `devBrand` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `devModel` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `devPDate` date NULL DEFAULT NULL,
  `devGPeriod` int(11) NULL DEFAULT NULL,
  `devSTime` date NULL DEFAULT NULL,
  `uCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `devStatus` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `devPhoto` char(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `devRemark` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `devSN` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbroles
-- ----------------------------
DROP TABLE IF EXISTS `tbroles`;
CREATE TABLE `tbroles`  (
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `rName` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `rDesc` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uCode` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `rEditTime` timestamp(0) NULL DEFAULT NULL,
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbunits
-- ----------------------------
DROP TABLE IF EXISTS `tbunits`;
CREATE TABLE `tbunits`  (
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `unitCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `maxUserNumber` int(11) NULL DEFAULT NULL,
  `maxVideoUserNumber` int(11) NULL DEFAULT NULL,
  `unitName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `unitLoginName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uHeadPortrait` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `PIC` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `Duty` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `Tel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `Province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `City` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `County` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `detailAddress` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `Remarks` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

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
  `ID` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `isDel` tinyint(1) NULL DEFAULT NULL,
  `recSN` blob NULL,
  `LastModifyTime` timestamp(0) NULL DEFAULT NULL,
  `uCode` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `csCode` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `latitude` decimal(10, 0) NULL DEFAULT NULL,
  `longitude` decimal(10, 0) NULL DEFAULT NULL,
  `positionName` varchar(260) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `altitude` decimal(10, 0) NULL DEFAULT NULL,
  `speed` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationX` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationY` decimal(10, 0) NULL DEFAULT NULL,
  `accelerationZ` decimal(10, 0) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

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
-- Records of tbapitokens
-- ----------------------------
INSERT INTO `tbapitokens` VALUES ('00ba8a40-603c-4d83-aee0-f5a39e053680', '1008', 'puwCMKwAqYrrWQbx', '2018-08-24 09:05:43', 0, 0x9107EDB65D09D688);
INSERT INTO `tbapitokens` VALUES ('0efcfc82-c9c8-4cd9-b417-e966fc3a0530', '1007', 'QVJbxpRXEWEtrWMg', '2018-08-24 09:02:27', 0, 0x7ADC8B425D09D688);
INSERT INTO `tbapitokens` VALUES ('2be4f64b-ff7e-4304-bada-acfb0c8670fc', '1001', 'T8WTbiKlVcADow43', '2018-08-22 16:11:53', 0, 0xD58B48EB0608D688);
INSERT INTO `tbapitokens` VALUES ('ac0c1c93-5093-4a6e-8f40-5f3ae42a14fd', '1009', 'ehQK3LjS39BHbEwS', '2018-08-22 16:12:30', 0, 0xA85D68010708D688);
INSERT INTO `tbapitokens` VALUES ('bfcf25af-33d6-4fcd-a4eb-a96bd40a2881', '1006', 'mzjJ3vmuyZ8eJequ', '2018-08-24 23:01:57', 0, 0x45090889D209D688);

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
-- Records of tbdiscussiongroups
-- ----------------------------
INSERT INTO `tbdiscussiongroups` VALUES ('5F4A7B23-95A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E298, '2018-08-15 22:11:45', '1', '交流大会', 'hexiang', NULL, '');
INSERT INTO `tbdiscussiongroups` VALUES ('610372E7-96A0-E811-96FE-509A4C2006E1', 0, 0x000000000000E2A2, '2018-08-15 22:24:23', '2', '保护首长', 'hexiang', NULL, '');
INSERT INTO `tbdiscussiongroups` VALUES ('610372E7-96A0-E811-96FE-509A4C2006E3', 0, 0x0000000000014066, '2018-08-15 22:24:23', '3', '开发测试大会', 'hexiang', NULL, '');

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
-- Table structure for tbcasemessages
-- ----------------------------
DROP TABLE IF EXISTS `tbcasemessages`;
CREATE TABLE `tbcasemessages`  (
  `isDel` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除',
  `recSN` blob NULL,
  `csCode` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `msgType` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息类型Text，Image，Audio，Video等',
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
-- Records of tbcasemessages
-- ----------------------------
INSERT INTO `tbcasemessages` VALUES (0, 0xB088D08BD009D688, '', 'Text', 'bhh', '', '1008', '2018-08-24 22:47:42', '00357598-4779-4bb7-8585-9a4897fd8f41', 0, 0, NULL, 'Group', '2');
INSERT INTO `tbcasemessages` VALUES (0, 0x5D3AAFD8A108D688, '', 'Text', '测试消息2', '', '1009', '2018-08-23 10:40:54', '010a4389-b3c5-4d81-88b4-f819b9595890', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0x9972CB4D5D09D688, '', 'Text', 'bjj', '', '1007', '2018-08-24 09:02:46', '06b09775-57bf-42cb-8e19-79b7133cde75', 0, 0, NULL, 'Group', '2');
INSERT INTO `tbcasemessages` VALUES (0, 0xE5BBF099A708D688, '', 'Text', '对的人', '', '1009', '2018-08-23 11:22:05', '109ba198-3974-4a80-a892-ad860cff5e99', 0, 0, NULL, 'Person', '1010');
INSERT INTO `tbcasemessages` VALUES (0, 0xEDD37666A708D688, '', 'Text', 'hhhh', '', '1009', '2018-08-23 11:20:39', '1184a6d4-9601-46de-9cba-d7dcc87ffb14', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0x4301DC435E09D688, '', 'Text', 'bjj', '', '1008', '2018-08-24 09:09:39', '29f614e3-c108-44a9-a2f8-793358bf7b8d', 0, 0, NULL, 'Group', '2');
INSERT INTO `tbcasemessages` VALUES (0, 0x85BA5A15A208D688, '', 'Text', '测试消息2', '', '1009', '2018-08-23 10:42:36', '2ebb017c-0061-422e-a1fa-86fd2acd5bc3', 0, 0, NULL, 'Person', '1010');
INSERT INTO `tbcasemessages` VALUES (0, 0x33FDE5B5F40BD688, '', 'Text', '哦哦懂', '', '1009', '2018-08-27 16:11:37', '31565e0c-71b2-47ec-9f8d-2e1e5dc77b6d', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0xC2D612545D09D688, '', 'Text', 'bnnt', '', '1007', '2018-08-24 09:02:57', '32c0e4ff-3b31-4fcb-9e9c-9c81b590f92e', 0, 0, NULL, 'Group', '2');
INSERT INTO `tbcasemessages` VALUES (0, 0xF10D88E7FB0BD688, '', 'Image', NULL, '0wMLYbmIrg8W9Q', '1009', '2018-08-27 17:03:07', '43a4beb2-aeaf-453e-9dd2-dfa048d75783', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0x08C561B0F40BD688, '', 'Text', '安', '', '1009', '2018-08-27 16:11:28', '46efb3fb-6d6d-4afc-80a8-405150dae609', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0x3699F798D209D688, '', 'Text', 'hello', '', '1006', '2018-08-24 23:02:23', '4abfb0c7-0ddd-445c-9d9d-ed828c5ac80b', 0, 0, NULL, 'Group', '2');
INSERT INTO `tbcasemessages` VALUES (0, 0xA92487425E09D688, '', 'Text', 'bjd', '', '1007', '2018-08-24 09:09:37', '57a425f0-3b75-4871-bbf0-3c85fa917d9e', 0, 0, NULL, 'Group', '2');
INSERT INTO `tbcasemessages` VALUES (0, 0x8B4671B8A108D688, '', 'Text', '测试消息1', '', '1009', '2018-08-23 10:40:00', '58afc287-072c-4b8e-80af-50aa7479eab6', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0xDE6B7FFCF10BD688, '', 'Text', '啦啊', '', '1009', '2018-08-27 15:52:07', '5d2010cd-0a4e-44c0-b121-4b502aaf2265', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0x49D11DDFA108D688, '', 'Text', '测试消息3', '', '1009', '2018-08-23 10:41:05', '6474d44d-2560-4365-a6ae-9adf79d7b180', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0x3C9DA17DF70BD688, '', 'Image', NULL, '335962eb-bb83-4dc9-af06-09962d1cc76e', '1009', '2018-08-27 16:31:31', '776196cb-9d0c-4bef-bb36-fa439e05da07', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0xFC434C10F50BD688, '', 'Text', '空', '', '1009', '2018-08-27 16:14:09', '8518c7bd-d7a4-4cc0-8c84-de37d4bc0893', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0x7FD4CD98A108D688, '', 'Text', '测试消息1', '', '1009', '2018-08-23 10:39:07', '91493568-4931-4d22-a091-bed60fcce577', 0, 0, NULL, 'Person', '1010');
INSERT INTO `tbcasemessages` VALUES (0, 0x459DBA465E09D688, '', 'Text', 'hjj', '', '1008', '2018-08-24 09:09:44', '94082a14-8061-42f2-b1b2-d19591130e3b', 0, 0, NULL, 'Group', '2');
INSERT INTO `tbcasemessages` VALUES (0, 0x31A06119A208D688, '', 'Text', '测试消息4', '', '1009', '2018-08-23 10:42:42', '9a8aacb8-5549-4050-803f-f97131068770', 0, 0, NULL, 'Person', '1010');
INSERT INTO `tbcasemessages` VALUES (0, 0x5184B1AAA708D688, '', 'Text', '哈哈哈', '', '1009', '2018-08-23 11:22:34', 'a0eecc7a-4819-411b-b4a9-b0d64fd1a933', 0, 0, NULL, 'Person', '1010');
INSERT INTO `tbcasemessages` VALUES (0, 0x39DBCDE1A108D688, '', 'Text', '测试消息4', '', '1009', '2018-08-23 10:41:09', 'bc3308d0-a3df-4a03-b059-d9b27330b4eb', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0x2148C3B1F90BD688, '', 'Image', NULL, '5BNmt8NnG6xBnb', '1009', '2018-08-27 16:47:18', 'c303a7dc-b18e-4fb3-b0f6-97b4ee90eae4', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0x89370059F60BD688, '', 'Image', NULL, '79d9c2b3-25a6-48e5-b95e-37a2c7c3f639', '1009', '2018-08-27 16:23:20', 'c82ef6af-3b6a-42f5-b282-b6f03c8afc03', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0xCE7EC0E3A108D688, '', 'Text', '测试消息5', '', '1009', '2018-08-23 10:41:12', 'cbcd277c-03ca-4d9d-8809-5e8c61d625c6', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0xD9B220B3F40BD688, '', 'Text', '恩', '', '1009', '2018-08-27 16:11:32', 'd276ed80-44b1-4a79-b845-ff9dd01d295f', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0x59D280BD8E09D688, '', 'Text', '也一样', '', '1009', '2018-08-24 14:56:39', 'd41385ea-db7f-4182-a64f-7331e6b71dec', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0xC0AE447B5E09D688, '', 'Text', 'vvbh', '', '1008', '2018-08-24 09:11:12', 'd9aa17f3-9a6c-4009-857b-446b29950701', 0, 0, NULL, 'Group', '2');
INSERT INTO `tbcasemessages` VALUES (0, 0xA2984917A208D688, '', 'Text', '测试消息3', '', '1009', '2018-08-23 10:42:39', 'e3307b42-357f-4ab4-9c66-c90d958c5759', 0, 0, NULL, 'Person', '1010');
INSERT INTO `tbcasemessages` VALUES (0, 0x5E14B0B5A708D688, '', 'Text', '骨头汤', '', '1009', '2018-08-23 11:22:52', 'f42bdcdc-e993-4b70-b346-bcd2487f2bd6', 0, 0, NULL, 'Group', '3');
INSERT INTO `tbcasemessages` VALUES (0, 0xCE877F808D09D688, '', 'Text', '哈哈哈', '', '1009', '2018-08-24 14:47:47', 'ffaefcd3-7f99-459b-a910-c12730544af8', 0, 0, NULL, 'Group', '3');

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
-- Records of tbfiledetails
-- ----------------------------
INSERT INTO `tbfiledetails` VALUES (0, 0x557D65B1F90BD688, NULL, '5BNmt8NnG6xBnb', '201808271623066.jpg', 341, NULL, NULL, NULL, NULL, '191e1871-c5f3-4d2c-a87d-54d699c10ecd', '/UploadFiles/20180827/201808271623066.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0xD92ADFADD10BD688, NULL, 'kw9J5ZSodvnb2O', '201808272492577.jpg', 391, NULL, NULL, NULL, NULL, '23be1253-7798-43d7-9b02-cdf00e307469', '/UploadFiles/20180827/201808272492577.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0x4355677DF70BD688, NULL, 'pRfK0siE2sjBmi', '201808274298532.jpg', 149, NULL, NULL, NULL, NULL, '335962eb-bb83-4dc9-af06-09962d1cc76e', '/UploadFiles/20180827/201808274298532.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0x2E7C84D9F50BD688, NULL, '3U3UU5bf2Ie3VQ', '201808276308302.jpg', 215, NULL, NULL, NULL, NULL, '3df58bfe-4cea-4c39-9259-4d1ec91a1723', '/UploadFiles/20180827/201808276308302.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0x6D8A7682D70BD688, NULL, 'tuanzrIKCKuEKE', '201808274042061.jpg', 440, NULL, NULL, NULL, NULL, '40811ad4-2eac-48a8-9664-585752cd4a96', '/UploadFiles/20180827/201808274042061.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0x0C0728E7FB0BD688, NULL, '0wMLYbmIrg8W9Q', '201808278517169.jpg', 215, NULL, NULL, NULL, NULL, '57ed3e6c-e6fa-4708-9c24-039032d3b27f', '/UploadFiles/20180827/201808278517169.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0x63D8855AD20BD688, NULL, 'lwK1kzuBAmGNsi', '201808271272604.jpg', 341, NULL, NULL, NULL, NULL, '672f7a0a-315e-4a2b-b336-1b2bbd5135eb', '/UploadFiles/20180827/201808271272604.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0x179A7D57D70BD688, NULL, 'ZvYgsVV9hmHYwQ', '201808274100630.jpg', 436, NULL, NULL, NULL, NULL, '79caa2f2-c580-43aa-9960-c6243130b8e5', '/UploadFiles/20180827/201808274100630.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0xD1639C58F60BD688, NULL, 'BJ8eO9pNWbw6G9', '201808275206830.jpg', 391, NULL, NULL, NULL, NULL, '79d9c2b3-25a6-48e5-b95e-37a2c7c3f639', '/UploadFiles/20180827/201808275206830.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0x2A0A93BAF40BD688, NULL, 'SQv40ph4JaX36j', '201808275480785.jpg', 391, NULL, NULL, NULL, NULL, '7f1dcc04-963e-43d7-8a73-e6a1d71302b3', '/UploadFiles/20180827/201808275480785.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0x5F271916F50BD688, NULL, 'zr1o54WRncSMlY', '201808271309767.jpg', 391, NULL, NULL, NULL, NULL, '84a11873-5001-447a-b9ef-cc17a4795089', '/UploadFiles/20180827/201808271309767.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0x5A861A7ED20BD688, NULL, '3oN7jKp8kL6de7', '201808277081404.jpg', 436, NULL, NULL, NULL, NULL, 'a948bf55-da39-4260-ac9e-419f449c3c3d', '/UploadFiles/20180827/201808277081404.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0xD6906BBBD20BD688, NULL, 'fa29rlAqG0dY6s', '201808275987610.jpg', 137, NULL, NULL, NULL, NULL, 'bc3306b0-19f9-4447-b51a-04f79de4eb7d', '/UploadFiles/20180827/201808275987610.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0x40B4F886D00BD688, NULL, 'fbtT3YM98ccvt4', '201808273303059.jpg', 391, NULL, NULL, NULL, NULL, 'c3f6ea5e-599e-4414-843b-e26d4bcf9587', '/UploadFiles/20180827/201808273303059.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0x6CAAA6D6D00BD688, NULL, 'v2ZHmzU4ZVv3UG', '201808279993483.jpg', 391, NULL, NULL, NULL, NULL, 'c4307b71-1db3-4347-871a-6b35b2b98193', '/UploadFiles/20180827/201808279993483.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0x7B4F84A7F50BD688, NULL, 'qdzBFTxqrJWIjF', '201808278741057.jpg', 149, NULL, NULL, NULL, NULL, 'ca14bcfa-c3fd-4205-9740-becfa401260e', '/UploadFiles/20180827/201808278741057.jpg', '');
INSERT INTO `tbfiledetails` VALUES (0, 0x8754DB71D70BD688, NULL, 'udAlq7vw3S7OAz', '201808276492015.jpg', 149, NULL, NULL, NULL, NULL, 'ece1de0e-1760-46d8-b0dc-9db552b19a49', '/UploadFiles/20180827/201808276492015.jpg', '');

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

-- ----------------------------
-- Records of tbusers
-- ----------------------------
INSERT INTO `tbusers` (isDel,recSN,uCode,uBelong,uIsActive,rName,pcNum,uName,uSex,uDuty,dCode,uTel,uShortNum,uPassword,ID,uHeadPortrait,dName,LYCID,loginFailTimes,lastLoginTime,uRemarks,Createtime,uDepartment,accountType,uEmployeenum,uIshistory,uIsUnilt,uIsAccontion,uUnitCode)
VALUES (0, 0x0000000000010290, '1001', NULL, 1, NULL, NULL, '陆警官', 0, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489', NULL, NULL, '06E6C0EA3096DA10229E4B8D9612BAC3', '4A2B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2986382168,4102092451&fm=27&gp=0.jpg', NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 'A61DEBB3-DF77-4731-942B-0187C931E489');
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

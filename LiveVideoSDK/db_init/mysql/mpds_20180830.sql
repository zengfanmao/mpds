/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50620
Source Host           : localhost:3306
Source Database       : mpds

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2018-09-18 10:02:45
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
  `deptid` varchar(64) DEFAULT NULL COMMENT '部门ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='用户关联组';

-- ----------------------------
-- Records of mpds_group_user
-- ----------------------------
INSERT INTO `mpds_group_user` VALUES ('14', '专案组', 'F0090', 'APP', 'admin', '超级管理员', '海珠区公安局', 'S0005', '2018-09-09 18:17:38', null);
INSERT INTO `mpds_group_user` VALUES ('15', '专案组2', 'F0092', 'PDT', 'admin', '超级管理员', null, null, '2018-09-09 18:17:38', null);
INSERT INTO `mpds_group_user` VALUES ('17', '专案组', 'F0090', 'APP', 'darren', '李小明', '海珠区公安局', 'S0005', '2018-09-09 18:21:42', null);
INSERT INTO `mpds_group_user` VALUES ('19', '专案组1', 'F0091', 'PDT', 'admin', '超级管理员', '天河区公安局', 'S0002', '2018-09-11 09:31:40', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='管理日志表';

-- ----------------------------
-- Records of mpds_log
-- ----------------------------
INSERT INTO `mpds_log` VALUES ('1', '444', '444', '4444', '444', null, null, '44', '2018-08-06 13:52:11');
INSERT INTO `mpds_log` VALUES ('2', 'admin', '超级管理员', 'Edit', '修改管理角色:超级管理员', null, null, '127.0.0.1', '2018-09-11 09:52:03');
INSERT INTO `mpds_log` VALUES ('3', 'admin', '超级管理员', 'Login', '用户登录', null, null, '127.0.0.1', '2018-09-14 09:57:39');
INSERT INTO `mpds_log` VALUES ('4', 'admin', '超级管理员', 'Login', '用户登录', null, null, '127.0.0.1', '2018-09-14 10:01:46');
INSERT INTO `mpds_log` VALUES ('5', 'admin', '超级管理员', 'Login', '用户登录', null, null, '127.0.0.1', '2018-09-18 09:25:25');
INSERT INTO `mpds_log` VALUES ('6', 'admin', '超级管理员', 'Edit', '修改用户:darren', null, null, '127.0.0.1', '2018-09-18 09:56:40');
INSERT INTO `mpds_log` VALUES ('7', 'admin', '超级管理员', 'Edit', '修改通话组:专案组', null, null, '127.0.0.1', '2018-09-18 09:58:33');

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
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=utf8 COMMENT='系统导航菜单';

-- ----------------------------
-- Records of mpds_menu
-- ----------------------------
INSERT INTO `mpds_menu` VALUES ('4', '0', 'System', 'sys_controller', '控制面板', '控制面板', '.icon-setting', '', '203', '0', '系统默认导航，不可更改导航ID', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('7', '4', 'System', 'sys_manager', '系统管理', '', '', '', '150', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('8', '7', 'System', 'manager_list', '用户管理', '', '', 'manager/manager_list.aspx', '99', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('9', '7', 'System', 'manager_role', '角色管理', '', '', 'manager/role_list.aspx', '100', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('10', '19', 'System', 'manager_log', '操作日志', '', '', 'manager/manager_log.aspx', '205', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('19', '4', 'System', 'sys_business', '业务管理', '', '', '', '202', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('20', '19', 'System', 'sys_group', '通讯组管理', '', '', 'manager/group_list.aspx', '99', '0', '', 'Show,View,Add,Edit,Delete,Build', '0');
INSERT INTO `mpds_menu` VALUES ('21', '19', 'System', 'casemessage', '语言回放', '', '', 'manager/casemessage_list.aspx', '100', '0', '', 'Show,View,Build', '0');
INSERT INTO `mpds_menu` VALUES ('22', '7', 'System', 'sys_navigation', '功能菜单', '', '', 'settings/nav_list.aspx', '202', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('26', '2', 'System', 'sys_plugin_manage', '插件管理', '', '', '', '99', '0', '', 'Show,View', '1');
INSERT INTO `mpds_menu` VALUES ('35', '23', 'System', 'order_manage', '订单管理', '', '', '', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('36', '23', 'System', 'order_settings', '订单设置', '', '', '', '100', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('37', '36', 'System', 'order_config', '订单参数设置', '', '', 'order/order_config.aspx', '99', '0', '', 'Show,View,Edit', '0');
INSERT INTO `mpds_menu` VALUES ('38', '36', 'System', 'order_payment', '支付方式设置', '', '', 'order/site_payment_list.aspx', '100', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('39', '36', 'System', 'order_express', '配送方式设置', '', '', 'order/express_list.aspx', '101', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('41', '35', 'System', 'order_confirm', '待确认订单', '', '', 'order/order_confirm.aspx', '99', '0', '', 'Show,View,Confirm', '0');
INSERT INTO `mpds_menu` VALUES ('42', '35', 'System', 'order_list', '全部订单', '', '', 'order/order_list.aspx', '100', '0', '', 'Show,View,Add,Edit,Delete,Confirm,Cancel,Invalid', '0');
INSERT INTO `mpds_menu` VALUES ('43', '3', 'System', 'user_manage', '会员管理', '', '', '', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('44', '43', 'System', 'user_audit', '审核会员', '', '', 'users/user_audit.aspx', '99', '0', '', 'Show,View,Audit', '0');
INSERT INTO `mpds_menu` VALUES ('45', '43', 'System', 'user_list', '所有会员', '', '', 'users/user_list.aspx', '100', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('46', '43', 'System', 'user_group', '会员组别', '', '', 'users/group_list.aspx', '101', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('47', '3', 'System', 'user_log', '会员日志', '', '', '', '100', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('48', '47', 'System', 'user_sms', '发送短信', '', '', 'users/user_sms.aspx', '99', '0', '', 'Show,View,Add', '0');
INSERT INTO `mpds_menu` VALUES ('49', '47', 'System', 'user_message', '站内消息', '', '', 'users/message_list.aspx', '100', '0', '', 'Show,View,Add,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('50', '47', 'System', 'user_recharge_log', '充值记录', '', '', 'users/recharge_list.aspx', '101', '0', '', 'Show,View,Add,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('51', '47', 'System', 'user_amount_log', '消费记录', '', '', 'users/amount_log.aspx', '102', '0', '', 'Show,View,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('52', '47', 'System', 'user_point_log', '积分记录', '', '', 'users/point_log.aspx', '103', '0', '', 'Show,View,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('53', '3', 'System', 'user_settings', '会员设置', '', '', '', '101', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('54', '53', 'System', 'user_config', '参数设置', '', '', 'users/user_config.aspx', '99', '0', '', 'Show,View,Edit', '0');
INSERT INTO `mpds_menu` VALUES ('55', '53', 'System', 'user_sms_template', '短信模板', '', '', 'users/sms_template_list.aspx', '101', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('56', '53', 'System', 'user_mail_template', '邮件模板', '', '', 'users/mail_template_list.aspx', '102', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('57', '53', 'System', 'user_oauth', 'OAuth设置', '', '', 'users/site_oauth_list.aspx', '100', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('80', '26', 'System', 'plugin_feedback', '留言管理', '', '', '/plugins/feedback/admin/index.aspx', '0', '0', '', 'Show,View,Delete,Audit,Reply', '1');
INSERT INTO `mpds_menu` VALUES ('81', '26', 'System', 'plugin_link', '链接管理', '', '', '/plugins/link/admin/index.aspx', '0', '0', '', 'Show,View,Add,Edit,Delete,Audit', '1');
INSERT INTO `mpds_menu` VALUES ('82', '58', 'System', 'weixin_base_settings', '基本设置', '', '', '', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('83', '82', 'System', 'weixin_account_manage', '公众平台管理', '', '', 'weixin/account_list.aspx', '99', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('84', '82', 'System', 'weixin_custom_menu', '自定义菜单', '', '', 'weixin/menu_edit.aspx', '100', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('85', '58', 'System', 'weixin_message_manage', '消息管理', '', '', '', '100', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('86', '85', 'System', 'weixin_subscribe_subscribe', '关注回复', '', '', 'weixin/subscribe_edit.aspx?action=subscribe', '99', '0', '', 'Show,View,Edit', '0');
INSERT INTO `mpds_menu` VALUES ('87', '85', 'System', 'weixin_subscribe_default', '默认回复', '', '', 'weixin/subscribe_edit.aspx?action=default', '100', '0', '', 'Show,View,Edit', '0');
INSERT INTO `mpds_menu` VALUES ('88', '85', 'System', 'weixin_response_text', '文本回复', '', '', 'weixin/text_list.aspx', '101', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('89', '85', 'System', 'weixin_response_picture', '图文回复', '', '', 'weixin/picture_list.aspx', '102', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('90', '85', 'System', 'weixin_response_sound', '语音回复', '', '', 'weixin/sound_list.aspx', '103', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('91', '85', 'System', 'weixin_response_content', '消息记录', '', '', 'weixin/response_list.aspx', '104', '0', '', 'Show,View,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('92', '1', 'System', 'channel_main', '默认站点', '', '', '', '99', '0', '', 'Show', '1');
INSERT INTO `mpds_menu` VALUES ('93', '92', 'System', 'channel_news', '新闻资讯', '', '', '', '99', '0', '', 'Show', '1');
INSERT INTO `mpds_menu` VALUES ('94', '93', 'System', 'channel_news_list', '内容管理', '', '', 'article/article_list.aspx', '99', '0', '', 'Show,View,Add,Edit,Delete,Audit', '1');
INSERT INTO `mpds_menu` VALUES ('95', '93', 'System', 'channel_news_category', '栏目类别', '', '', 'article/category_list.aspx', '100', '0', '', 'Show,View,Add,Edit,Delete', '1');
INSERT INTO `mpds_menu` VALUES ('96', '93', 'System', 'channel_news_comment', '评论管理', '', '', 'article/comment_list.aspx', '103', '0', '', 'Show,View,Delete,Reply', '1');
INSERT INTO `mpds_menu` VALUES ('108', '92', 'System', 'channel_goods', '购物商城', '', '', '', '100', '0', '', 'Show', '1');
INSERT INTO `mpds_menu` VALUES ('109', '108', 'System', 'channel_goods_list', '内容管理', '', '', 'article/article_list.aspx', '99', '0', '', 'Show,View,Add,Edit,Delete,Audit', '1');
INSERT INTO `mpds_menu` VALUES ('110', '108', 'System', 'channel_goods_category', '栏目类别', '', '', 'article/category_list.aspx', '100', '0', '', 'Show,View,Add,Edit,Delete', '1');
INSERT INTO `mpds_menu` VALUES ('111', '108', 'System', 'channel_goods_spec', '规格管理', '', '', 'article/spec_list.aspx', '102', '0', '', 'Show,View,Add,Edit,Delete', '1');
INSERT INTO `mpds_menu` VALUES ('112', '108', 'System', 'channel_goods_comment', '评论管理', '', '', 'article/comment_list.aspx', '103', '0', '', 'Show,View,Delete,Reply', '1');
INSERT INTO `mpds_menu` VALUES ('113', '92', 'System', 'channel_video', '视频专区', '', '', '', '101', '0', '', 'Show', '1');
INSERT INTO `mpds_menu` VALUES ('114', '113', 'System', 'channel_video_list', '内容管理', '', '', 'article/article_list.aspx', '99', '0', '', 'Show,View,Add,Edit,Delete,Audit', '1');
INSERT INTO `mpds_menu` VALUES ('115', '113', 'System', 'channel_video_category', '栏目类别', '', '', 'article/category_list.aspx', '100', '0', '', 'Show,View,Add,Edit,Delete', '1');
INSERT INTO `mpds_menu` VALUES ('116', '113', 'System', 'channel_video_comment', '评论管理', '', '', 'article/comment_list.aspx', '103', '0', '', 'Show,View,Delete,Reply', '1');
INSERT INTO `mpds_menu` VALUES ('117', '92', 'System', 'channel_photo', '图片分享', '', '', '', '102', '0', '', 'Show', '1');
INSERT INTO `mpds_menu` VALUES ('118', '117', 'System', 'channel_photo_list', '内容管理', '', '', 'article/article_list.aspx', '99', '0', '', 'Show,View,Add,Edit,Delete,Audit', '1');
INSERT INTO `mpds_menu` VALUES ('119', '117', 'System', 'channel_photo_category', '栏目类别', '', '', 'article/category_list.aspx', '100', '0', '', 'Show,View,Add,Edit,Delete', '1');
INSERT INTO `mpds_menu` VALUES ('120', '117', 'System', 'channel_photo_comment', '评论管理', '', '', 'article/comment_list.aspx', '103', '0', '', 'Show,View,Delete,Reply', '1');
INSERT INTO `mpds_menu` VALUES ('121', '92', 'System', 'channel_down', '资源下载', '', '', '', '103', '0', '', 'Show', '1');
INSERT INTO `mpds_menu` VALUES ('122', '121', 'System', 'channel_down_list', '内容管理', '', '', 'article/article_list.aspx', '99', '0', '', 'Show,View,Add,Edit,Delete,Audit', '1');
INSERT INTO `mpds_menu` VALUES ('123', '121', 'System', 'channel_down_category', '栏目类别', '', '', 'article/category_list.aspx', '100', '0', '', 'Show,View,Add,Edit,Delete', '1');
INSERT INTO `mpds_menu` VALUES ('124', '121', 'System', 'channel_down_comment', '评论管理', '', '', 'article/comment_list.aspx', '103', '0', '', 'Show,View,Delete,Reply', '1');
INSERT INTO `mpds_menu` VALUES ('125', '92', 'System', 'channel_content', '公司介绍', '', '', '', '104', '0', '', 'Show', '1');
INSERT INTO `mpds_menu` VALUES ('126', '125', 'System', 'channel_content_list', '内容管理', '', '', 'article/article_list.aspx', '99', '0', '', 'Show,View,Add,Edit,Delete,Audit', '1');
INSERT INTO `mpds_menu` VALUES ('127', '125', 'System', 'channel_content_category', '栏目类别', '', '', 'article/category_list.aspx', '100', '0', '', 'Show,View,Add,Edit,Delete', '1');
INSERT INTO `mpds_menu` VALUES ('132', '7', 'System', 'sys_dept', '组织机构', '', '', 'manager/dept_list.aspx', '50', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('133', '0', 'System', 'Index', '系统首页', '系统首页', '.icon-home', '', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('134', '133', 'System', 'manager_index', '首页管理', '首页管理', '', '', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('135', '134', 'System', 'sys_index', '系统首页', '', '', 'center.aspx', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('136', '0', 'APP', 'app_main', 'APP管理', 'APP管理', '', '', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('137', '136', 'APP', 'app_group', 'APP群组', '', '', '', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('138', '136', 'APP', 'app_person', 'APP个人', '', '', '', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('139', '136', 'APP', 'app_gps', 'APP定位', '', '', '', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('140', '136', 'APP', 'app_recording', 'APP回放', '', '', '', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('141', '137', 'APP', 'app_group_cut', '中断组呼', '', '', 'APP', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('142', '138', 'APP', 'app_person_stop', 'APP遥晕', '', '', 'APP', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('143', '138', 'APP', 'app_person_kill', 'APP遥毙', '', '', 'APP', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('144', '138', 'APP', 'app_person_disable', 'APP禁用', '', '', 'APP', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('145', '138', 'APP', 'app_person_enable', 'APP激活', '', '', 'APP', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('146', '138', 'APP', 'app_person_cut', '中断个呼', '', '', 'APP', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('147', '139', 'APP', 'app_gps_search', '定位查询', '', '', 'APP', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('148', '140', 'APP', 'app_recording_search', '录音回放查询', '', '', 'APP', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('149', '138', 'APP', 'app_person_search', 'APP个人查询', '', '', 'APP', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('155', '19', 'APP', 'manager_appversion', 'APP版本', 'APP版本', '', 'manager/appversion_list.aspx', '150', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('156', '137', 'APP', 'app_group_call', 'APP组呼', '', '', 'APP', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('157', '138', 'APP', 'app_person_call', 'APP个呼', '', '', 'APP', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('158', '137', 'APP', 'app_group_charge', 'APP抢话', '', '', 'APP', '99', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` (`id`, `parent_id`, `nav_type`, `name`, `title`, `sub_title`, `icon_url`, `link_url`, `sort_id`, `is_lock`, `remark`, `action_type`, `is_sys`) VALUES ('159', '19', 'System', 'sys_rank', '组群管理', '', '', 'manager/rank_list.aspx', '99', '0', '', 'Show,View,Add,Edit,Delete', '0');

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
  `role_level` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='管理角色表';

-- ----------------------------
-- Records of mpds_role
-- ----------------------------
INSERT INTO `mpds_role` VALUES ('1', '超级管理员', '1', '1', '超级管理员，拥有所有权限', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8 COMMENT='管理角色权限表';

-- ----------------------------
-- Records of mpds_role_value
-- ----------------------------

-- ----------------------------
-- Table structure for tbapitokens
-- ----------------------------
DROP TABLE IF EXISTS `tbapitokens`;
CREATE TABLE `tbapitokens` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `uCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户编号',
  `apiToken` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密钥',
  `createdTime` timestamp NULL DEFAULT NULL,
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Api访问密钥';

-- ----------------------------
-- Records of tbapitokens
-- ----------------------------

-- ----------------------------
-- Table structure for tbapplogs
-- ----------------------------
DROP TABLE IF EXISTS `tbapplogs`;
CREATE TABLE `tbapplogs` (
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
-- Records of tbapplogs
-- ----------------------------

-- ----------------------------
-- Table structure for tbappversions
-- ----------------------------
DROP TABLE IF EXISTS `tbappversions`;
CREATE TABLE `tbappversions` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `isDel` int(11) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP版本管理';

-- ----------------------------
-- Records of tbappversions
-- ----------------------------
INSERT INTO `tbappversions` VALUES ('1', '0', null, '', '3', '1.0', '2018-09-12 15:35:39', '', '融合对讲APP', '/upload/201809/08/201809081725012623.apk', 'admin');

-- ----------------------------
-- Table structure for tbcasecollection
-- ----------------------------
DROP TABLE IF EXISTS `tbcasecollection`;
CREATE TABLE `tbcasecollection` (
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
-- Records of tbcasecollection
-- ----------------------------

-- ----------------------------
-- Table structure for tbcasedeploydevs
-- ----------------------------
DROP TABLE IF EXISTS `tbcasedeploydevs`;
CREATE TABLE `tbcasedeploydevs` (
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `csCode` char(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `devCode` char(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uCode` char(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbcasedeploydevs
-- ----------------------------

-- ----------------------------
-- Table structure for tbcasedeploys
-- ----------------------------
DROP TABLE IF EXISTS `tbcasedeploys`;
CREATE TABLE `tbcasedeploys` (
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `csCode` char(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uCode` char(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rName` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbcasedeploys
-- ----------------------------

-- ----------------------------
-- Table structure for tbcasegoings
-- ----------------------------
DROP TABLE IF EXISTS `tbcasegoings`;
CREATE TABLE `tbcasegoings` (
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
-- Records of tbcasegoings
-- ----------------------------

-- ----------------------------
-- Table structure for tbcasegps
-- ----------------------------
DROP TABLE IF EXISTS `tbcasegps`;
CREATE TABLE `tbcasegps` (
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
-- Records of tbcasegps
-- ----------------------------

-- ----------------------------
-- Table structure for tbcaselivingshows
-- ----------------------------
DROP TABLE IF EXISTS `tbcaselivingshows`;
CREATE TABLE `tbcaselivingshows` (
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
-- Records of tbcaselivingshows
-- ----------------------------

-- ----------------------------
-- Table structure for tbcasemessages
-- ----------------------------
DROP TABLE IF EXISTS `tbcasemessages`;
CREATE TABLE `tbcasemessages` (
  `ID` bigint(13) NOT NULL AUTO_INCREMENT COMMENT '消息主键',
  `isDel` tinyint(1) DEFAULT NULL COMMENT '逻辑删除',
  `recSN` blob,
  `csCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `msgType` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息类型Text，Image，Audio，Video，Map, Command等',
  `msgAbstract` longtext COLLATE utf8mb4_unicode_ci COMMENT '消息内容，文字内容, 命令内容',
  `msgFile` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息相关文件id，如发送图片的文件id',
  `uCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送用户编号',
  `msgTime` timestamp NULL DEFAULT NULL COMMENT '发送时间',
  `uLatitude` decimal(10,0) DEFAULT NULL COMMENT '发送人当前纬度',
  `uLongitude` decimal(10,0) DEFAULT NULL COMMENT '发送人当前经度',
  `uPositionName` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送人当前位置',
  `msgFromType` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息来源,Group,Person,LivingRoom等',
  `msgToCode` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送给谁，讨论组则讨论组id，个人则是个人编号，直播室则是直播室id',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='IM消息';

-- ----------------------------
-- Records of tbcasemessages
-- ----------------------------
INSERT INTO `tbcasemessages` VALUES ('1', null, null, null, 'Recording', '你好', '33333', 'darren', '2018-09-26 11:13:38', '13', '13', '33', 'Group', '333');

-- ----------------------------
-- Table structure for tbcasemission
-- ----------------------------
DROP TABLE IF EXISTS `tbcasemission`;
CREATE TABLE `tbcasemission` (
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
-- Records of tbcasemission
-- ----------------------------

-- ----------------------------
-- Table structure for tbcasemissiondistribution
-- ----------------------------
DROP TABLE IF EXISTS `tbcasemissiondistribution`;
CREATE TABLE `tbcasemissiondistribution` (
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
-- Records of tbcasemissiondistribution
-- ----------------------------

-- ----------------------------
-- Table structure for tbcases
-- ----------------------------
DROP TABLE IF EXISTS `tbcases`;
CREATE TABLE `tbcases` (
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
-- Records of tbcases
-- ----------------------------

-- ----------------------------
-- Table structure for tbdepartments
-- ----------------------------
DROP TABLE IF EXISTS `tbdepartments`;
CREATE TABLE `tbdepartments` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `isDel` int(11) DEFAULT NULL,
  `recSN` blob,
  `dCode` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dName` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dFather` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dDesc` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sort_id` int(11) DEFAULT NULL COMMENT '排序',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `farther` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '上级部门名称',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='部门';

-- ----------------------------
-- Records of tbdepartments
-- ----------------------------
INSERT INTO `tbdepartments` VALUES ('1', '0', null, 'S0001', '广州市公安局', '', null, '90', '2018-08-06 13:52:11', '2018-08-06 13:52:11', null);
INSERT INTO `tbdepartments` VALUES ('2', '0', null, 'S0002', '天河区公安局', 'S0001', null, '99', '2018-08-06 13:52:30', '2018-08-06 13:52:30', '广州市公安局');
INSERT INTO `tbdepartments` VALUES ('8', '0', null, 'S0005', '海珠区公安局', 'S0001', null, '99', '2018-09-03 10:03:37', '2018-09-03 10:03:37', '广州市公安局');

-- ----------------------------
-- Table structure for tbdeviceimdatas
-- ----------------------------
DROP TABLE IF EXISTS `tbdeviceimdatas`;
CREATE TABLE `tbdeviceimdatas` (
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
-- Records of tbdeviceimdatas
-- ----------------------------

-- ----------------------------
-- Table structure for tbdevices
-- ----------------------------
DROP TABLE IF EXISTS `tbdevices`;
CREATE TABLE `tbdevices` (
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
-- Records of tbdevices
-- ----------------------------

-- ----------------------------
-- Table structure for tbdiscussiongroupmenbers
-- ----------------------------
DROP TABLE IF EXISTS `tbdiscussiongroupmenbers`;
CREATE TABLE `tbdiscussiongroupmenbers` (
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
-- Records of tbdiscussiongroupmenbers
-- ----------------------------

-- ----------------------------
-- Table structure for tbdiscussiongroups
-- ----------------------------
DROP TABLE IF EXISTS `tbdiscussiongroups`;
CREATE TABLE `tbdiscussiongroups` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `isDel` int(11) DEFAULT NULL COMMENT '逻辑删除',
  `recSN` blob DEFAULT NULL,
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
  `deptid` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门ID',
  `status` int(11) DEFAULT NULL COMMENT '0 - 启用； 1 - 禁用 与isDel取值一致',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='通话组';

-- ----------------------------
-- Records of tbdiscussiongroups
-- ----------------------------
INSERT INTO `tbdiscussiongroups` VALUES ('1', '0', '', '2018-09-13 13:59:21', 'F0090', '专案组', 'admin', '超级管理员', null, 'APP', '专案组', 'F009', '海珠区公安局', 'S0005', '0', '2018-09-18 09:58:33');
INSERT INTO `tbdiscussiongroups` VALUES ('5', '0', '', '2018-09-09 15:57:16', 'F0091', '专案组1', 'admin', '超级管理员', null, 'PDT', null, null, '天河区公安局', 'S0002', '0', '2018-09-09 15:58:14');
INSERT INTO `tbdiscussiongroups` VALUES ('6', '0', '', '2018-09-09 15:57:59', 'F0092', '专案组2', 'admin', '超级管理员', null, 'PDT', null, null, null, null, '0', '2018-09-09 15:57:59');

-- ----------------------------
-- Table structure for tbfiledetails
-- ----------------------------
DROP TABLE IF EXISTS `tbfiledetails`;
CREATE TABLE `tbfiledetails` (
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
  `virtualId` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '虚拟id，比如关联讨论组，可以根据id查所有讨论组或者其他的的文件',
  `fduration` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='消息内容相关文件';

-- ----------------------------
-- Records of tbfiledetails
-- ----------------------------
INSERT INTO `tbfiledetails` VALUES (null, null, null, '33333', null, null, null, null, null, null, '', '../../upload/201809/14/1.mp3', null, null);

-- ----------------------------
-- Table structure for tbroles
-- ----------------------------
DROP TABLE IF EXISTS `tbroles`;
CREATE TABLE `tbroles` (
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
-- Records of tbroles
-- ----------------------------

-- ----------------------------
-- Table structure for tbunits
-- ----------------------------
DROP TABLE IF EXISTS `tbunits`;
CREATE TABLE `tbunits` (
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
-- Records of tbunits
-- ----------------------------

-- ----------------------------
-- Table structure for tbuserimdatas
-- ----------------------------
DROP TABLE IF EXISTS `tbuserimdatas`;
CREATE TABLE `tbuserimdatas` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
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
-- Records of tbuserimdatas
-- ----------------------------

-- ----------------------------
-- Table structure for tbusers
-- ----------------------------
DROP TABLE IF EXISTS `tbusers`;
CREATE TABLE `tbusers` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `isDel` int(4) DEFAULT NULL COMMENT '逻辑删除',
  `recSN` blob,
  `uCode` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户编号',
  `uPassword` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `uSalt` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uBelong` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
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
  `uIsUnilt` int(11) DEFAULT 0,
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
INSERT INTO `tbusers` VALUES ('1', '0', null, 'admin', '87FA6AD6CBFDF3108E4DD6F47F5D04A4', '24V0XZ', '', null, '超级管理员', null, '超级管理员', '男', '警监', 'S0002', '13789091234', '44400', '', '天河区公安局', null, null, null, null, '2018-09-04 08:33:19', '天河区公安局', '其他', null, null, null, null, '', '1', '1', 'F0091', '专案组1', null, null, null, '离线');
INSERT INTO `tbusers` VALUES ('7', '0', null, 'darren', '91FC8F5D0BC7B906', '884424', '', '0', '超级管理员', '', '李小明', '男', '警员', 'S0005', '13809782354', '334674', '', '海珠区公安局', null, null, null, null, '2018-09-03 15:01:10', '海珠区公安局', '特种警察', null, null, null, null, '', '1', '1', 'F009', '专案组', '5', '', null, '离线');


DROP TABLE IF EXISTS `mpds_audio_detail`;
CREATE TABLE `mpds_audio_detail` (
`id`  bigint(20) NOT NULL ,
`conference_uuid`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`conference_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`event_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`event_time`  datetime NULL DEFAULT NULL ,
`startor`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`listener`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`content`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
CHECKSUM=0
ROW_FORMAT=DYNAMIC
DELAY_KEY_WRITE=0
;


alter table tbfiledetails add index `fCode` (`fCode`) USING BTREE ;
alter table tbcasemessages add index `file_index` (`msgFile`) USING BTREE ;
alter table tbcasemessages add constraint file_index foreign key(`msgFile`) REFERENCES `tbfiledetails` (`fCode`) ON DELETE RESTRICT ON UPDATE RESTRICT;
alter table mpds_group_user add isdefault char(1) default 0;
ALTER TABLE `tbcasegps` MODIFY COLUMN `uCode`  varchar(20);
ALTER TABLE `tbuserimdatas` MODIFY COLUMN `uCode`  varchar(20);
alter table tbdepartments add dAbbr varchar(32);
alter table tbusers add purpose varchar(4);

alter table mpds_group_user add index `group_idx` (`groupid`) USING BTREE ;
alter table mpds_group_user add index `account_idx` (`account`) USING BTREE ;
alter table mpds_rank_group add index `group_idx` (`discussionCode`) USING BTREE ;
alter table tbdiscussiongroups add index `group_idx` (`discussionCode`) USING BTREE ;

alter table tbdiscussiongroups add usercount int(11) default 0;
alter table tbusers add UNIQUE INDEX `ucode_index` (`uCode`) USING BTREE;

-- 2019.04.19
alter table tbusers add INDEX `dcode_index` (`dCode`) USING BTREE;
alter table tbdepartments add alluser int(11);
alter table tbdepartments add onlineuser int(11);
alter table tbcasemessages add msgToName varchar(64);
alter table tbcasemessages add uName varchar(64);

alter table mpds_audio_detail add INDEX `uuid_index` (`conference_uuid`) USING BTREE;

ALTER TABLE tbdiscussiongroups ADD COLUMN clazz int(2) NULL DEFAULT 0 COMMENT '0 - 普通组； 1 - 授权组' AFTER `usercount`;


-- ----------------------------
-- Table structure for mpds_msg_count
-- ----------------------------
DROP TABLE IF EXISTS `mpds_msg_count`;
CREATE TABLE `mpds_msg_count` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `ucode` varchar(32) DEFAULT NULL,
  `receive` int(11) DEFAULT NULL,
  `send` int(11) DEFAULT NULL,
  `tocode` varchar(32) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

DELIMITER $
DROP TRIGGER casemessage_insert$
CREATE TRIGGER casemessage_insert BEFORE INSERT ON tbcasemessages FOR EACH ROW
BEGIN
  DECLARE fromcode varchar(32);
  DECLARE tocode varchar(32);
  DECLARE fromName varchar(64);
  DECLARE toName varchar(64);
  IF NEW.msgType != 'Recording' and NEW.msgType != 'Command' THEN
    IF NEW.msgFromType = 'Group' THEN
      SELECT a.ucode INTO tocode FROM mpds_msg_count a WHERE a.ucode = NEW.msgToCode AND type = 'Group';
      IF tocode is not null THEN
        UPDATE mpds_msg_count SET receive = receive+1 WHERE ucode = tocode AND type = 'Group';
      ELSE 
        INSERT INTO mpds_msg_count(ucode, receive, send, type) VALUE(NEW.msgToCode, 1, 0, 'Group');
      END IF;
    END IF;
    IF NEW.msgFromType = 'Person' THEN
      SELECT a.ucode,a.tocode INTO fromcode,tocode FROM mpds_msg_count a WHERE a.ucode = NEW.uCode AND a.tocode = NEW.msgToCode;
      IF fromcode is not null THEN
        UPDATE mpds_msg_count a SET a.send = a.send+1 WHERE a.ucode = fromcode AND a.tocode = tocode AND type = 'Person';
      ELSE 
        INSERT INTO mpds_msg_count(ucode, receive, send, tocode, type) VALUE(NEW.uCode, 0, 1, NEW.msgToCode, 'Person');
      END IF;
      -- SELECT a.ucode,a.tocode INTO fromcode,tocode FROM mpds_msg_count a WHERE a.ucode = NEW.msgToCode AND a.tocode = NEW.uCode;
      -- IF fromcode is not null THEN
      --  UPDATE mpds_msg_count a SET a.receive = a.receive+1 WHERE a.ucode = fromcode AND a.tocode = tocode AND type = 'Person';
      -- ELSE 
      -- INSERT INTO mpds_msg_count(ucode, receive, send, tocode, type) VALUE(NEW.msgToCode, 1, 0, NEW.uCode, 'Person');
      -- END IF;
    END IF;
  END IF;
  IF NEW.msgFromType = 'Group' THEN
    select a.uName into fromName from tbusers a where a.uCode = NEW.uCode;
    select a.discussionName into toName from tbDiscussionGroups a where a.discussionCode = NEW.msgToCode;
    SET NEW.uName = fromName;
    SET NEW.msgToName = toName;
  END IF;
  IF NEW.msgFromType = 'Person' THEN
    select a.uName into fromName from tbusers a where a.uCode = NEW.uCode;
    select a.uName into toName from tbusers a where a.uCode = NEW.msgToCode;
    SET NEW.uName = fromName;
    SET NEW.msgToName = toName;
  END IF;
END $
DELIMITER ;


-- ----------------------------
-- Table structure for mpds_rank
-- ----------------------------
DROP TABLE IF EXISTS `mpds_rank`;
CREATE TABLE `mpds_rank` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `rank` varchar(10) DEFAULT NULL,
  `rankName` varchar(64) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mpds_rank_group
-- ----------------------------
DROP TABLE IF EXISTS `mpds_rank_group`;
CREATE TABLE `mpds_rank_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `rank` varchar(10) DEFAULT NULL,
  `rankName` varchar(64) DEFAULT NULL,
  `discussionCode` varchar(10) DEFAULT NULL,
  `discussionName` varchar(64) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;


DELIMITER $
DROP TRIGGER mpds_group_user_count$
CREATE TRIGGER mpds_group_user_count AFTER UPDATE ON tbusers FOR EACH ROW
BEGIN
  IF NEW.devicetype = 'APP' THEN
    IF NEW.groupid = OLD.groupid and NEW.status = '在线' and OLD.status != '在线' THEN
      update tbdiscussiongroups a set a.usercount = a.usercount + 1 where a.discussionCode = NEW.groupid;
    END IF;
    IF NEW.groupid = OLD.groupid and NEW.status != '在线' and OLD.status = '在线' THEN
      update tbdiscussiongroups a set a.usercount = a.usercount - 1 where a.discussionCode = NEW.groupid;
    END IF;
    IF NEW.groupid != OLD.groupid and OLD.status = '在线' and NEW.status = '在线' THEN
      update tbdiscussiongroups a set a.usercount = a.usercount + 1 where a.discussionCode = NEW.groupid;
      update tbdiscussiongroups a set a.usercount = a.usercount - 1 where a.discussionCode = OLD.groupid;
    END IF;
    IF NEW.groupid != OLD.groupid and OLD.status = '在线' and NEW.status != '在线' THEN
      update tbdiscussiongroups a set a.usercount = a.usercount - 1 where a.discussionCode = OLD.groupid;
    END IF;
    IF NEW.groupid != OLD.groupid and OLD.status != '在线' and NEW.status = '在线' THEN
      update tbdiscussiongroups a set a.usercount = a.usercount + 1 where a.discussionCode = NEW.groupid;
    END IF;
  ELSE
    IF NEW.groupid = OLD.groupid and NEW.status = '在线' and OLD.status != '在线' THEN
      update tbdiscussiongroups a set a.usercount = a.usercount + 1 where a.relativegroupid = NEW.groupid;
    END IF;
    IF NEW.groupid = OLD.groupid and NEW.status != '在线' and OLD.status = '在线' THEN
      update tbdiscussiongroups a set a.usercount = a.usercount - 1 where a.relativegroupid = NEW.groupid;
    END IF;
    IF NEW.groupid != OLD.groupid and OLD.status = '在线' and NEW.status = '在线' THEN
      update tbdiscussiongroups a set a.usercount = a.usercount + 1 where a.relativegroupid = NEW.groupid;
      update tbdiscussiongroups a set a.usercount = a.usercount - 1 where a.relativegroupid = OLD.groupid;
    END IF;
    IF NEW.groupid != OLD.groupid and OLD.status = '在线' and NEW.status != '在线' THEN
      update tbdiscussiongroups a set a.usercount = a.usercount - 1 where a.relativegroupid = OLD.groupid;
    END IF;
    IF NEW.groupid != OLD.groupid and OLD.status != '在线' and NEW.status = '在线' THEN
      update tbdiscussiongroups a set a.usercount = a.usercount + 1 where a.relativegroupid = NEW.groupid;
    END IF;
  END IF;
END $
DELIMITER ;


update tbdiscussiongroups a 
set usercount = (SELECT count(*) FROM tbusers m WHERE m.groupid = a.discussionCode AND m.isDel = 0 AND m.status = '在线' AND m.devicetype = 'APP')
+ 
(select count(*) from tbDiscussionGroups s inner join tbUsers d on s.relativegroupid = d.groupid where s.discussionCode = a.discussionCode and s.type = 'APP' and d.devicetype = 'PDT' and d.status = '在线')
where a.type = 'APP';



CREATE DEFINER=`root`@`%` 
EVENT `dept_user_count`
ON SCHEDULE EVERY 30 SECOND STARTS '2018-10-12 13:34:15'
ON COMPLETION NOT PRESERVE
ENABLE
DO
update tbdepartments a set alluser = (select count(1) from tbusers b where b.dCode = a.dCode),
onlineuser = (select count(1) from tbusers c where c.dCode = a.dCode and c.`status` = '在线');

-- 2019.04.19
CREATE DEFINER=`root`@`%` 
EVENT `user_status_check`
ON SCHEDULE EVERY 10 SECOND STARTS '2018-10-12 13:34:15'
ON COMPLETION NOT PRESERVE
ENABLE
DO
update tbusers set status = '离线' 
where status = '在线' and devicetype = 'APP' 
and UNIX_TIMESTAMP(now())*1000 > (LYCID + 300000);


CREATE DEFINER=`root`@`%` 
EVENT `mpds_group_user_count`
ON SCHEDULE EVERY 300 SECOND STARTS '2018-10-12 13:34:15'
ON COMPLETION NOT PRESERVE
ENABLE
DO
update tbdiscussiongroups a 
set usercount = (SELECT count(*) FROM tbusers m WHERE m.groupid = a.discussionCode AND m.isDel = 0 AND m.status = '在线' AND m.devicetype = 'APP')
+ 
(select count(*) from tbDiscussionGroups s inner join tbUsers d on s.relativegroupid = d.groupid where s.discussionCode = a.discussionCode and s.type = 'APP' and d.devicetype = 'PDT' and d.status = '在线')
where a.type = 'APP';


create view group_user_count as 
SELECT
  s.discussionCode as groupid, count(*) as usercount
FROM
  tbDiscussionGroups s
INNER JOIN tbUsers d ON s.relativegroupid = d.groupid
WHERE
s.type = 'APP'
AND d.devicetype = 'PDT'
AND d. STATUS = '在线'
GROUP BY s.discussionCode
UNION
SELECT
  m.groupid,
  count(*) as usercount
FROM
  tbusers m
WHERE
m.isDel = 0
AND m.`STATUS` = '离线'
AND m.devicetype = 'APP'
GROUP BY m.groupid；

update tbdiscussiongroups a 
set usercount = 
(select sum(m.usercount) FROM group_user_count m WHERE m.groupid = a.discussionCode group BY m.groupid);


ALTER TABLE `tbcasemessages`
MODIFY COLUMN `uLatitude`  double(10,5) NULL DEFAULT NULL COMMENT '发送人当前纬度' AFTER `msgTime`,
MODIFY COLUMN `uLongitude`  double(10,5) NULL DEFAULT NULL COMMENT '发送人当前经度' AFTER `uLatitude`;

ALTER TABLE `tbapitokens` ADD UNIQUE INDEX `user_code` (`uCode`) USING HASH ;
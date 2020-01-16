/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50620
Source Host           : localhost:3306
Source Database       : pmessage

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2018-08-06 16:24:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mpds_dept
-- ----------------------------
DROP TABLE IF EXISTS `mpds_dept`;
CREATE TABLE `mpds_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `parent_id` int(11) DEFAULT NULL,
  `sort_id` int(11) DEFAULT NULL COMMENT '排序',
  `add_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint(4) DEFAULT '0' COMMENT '0-正常，1-删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='组织结构，按树形结构组织';

-- ----------------------------
-- Records of mpds_dept
-- ----------------------------
INSERT INTO `mpds_dept` VALUES ('5', '广州市公安局', '0', '99', '2018-08-06 13:52:11', '0');
INSERT INTO `mpds_dept` VALUES ('6', '天河区公安局', '5', '99', '2018-08-06 13:52:30', '0');
INSERT INTO `mpds_dept` VALUES ('7', '越秀区公安局', '5', '99', '2018-08-06 13:52:55', '0');
INSERT INTO `mpds_dept` VALUES ('8', '珠吉派出所', '6', '99', '2018-08-06 13:53:16', '0');
INSERT INTO `mpds_dept` VALUES ('9', '白云区公安局', '5', '99', '2018-08-06 13:53:34', '0');
INSERT INTO `mpds_dept` VALUES ('10', '荔湾区公安局', '5', '99', '2018-08-06 13:53:48', '0');
INSERT INTO `mpds_dept` VALUES ('11', '佛山市公安局', '0', '200', '2018-08-06 13:54:04', '0');
INSERT INTO `mpds_dept` VALUES ('12', '禅城公安局', '11', '99', '2018-08-06 13:54:47', '0');

-- ----------------------------
-- Table structure for mpds_group
-- ----------------------------
DROP TABLE IF EXISTS `mpds_group`;
CREATE TABLE `mpds_group` (
  `id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `number` varchar(16) DEFAULT NULL COMMENT '群编号',
  `type` char(1) DEFAULT '0' COMMENT '0 - 普通； 1 - 临时',
  `members` int(4) DEFAULT NULL COMMENT '成员个数',
  `onlines` int(4) DEFAULT NULL COMMENT '在线个数',
  `status` char(1) NOT NULL COMMENT '0 - 启用； 1 - 禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='群组信息';

-- ----------------------------
-- Records of mpds_group
-- ----------------------------

-- ----------------------------
-- Table structure for mpds_log
-- ----------------------------
DROP TABLE IF EXISTS `mpds_log`;
CREATE TABLE `mpds_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户名',
  `action_type` varchar(100) DEFAULT NULL COMMENT '操作类型',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `user_ip` varchar(30) DEFAULT NULL COMMENT '用户IP',
  `add_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理日志表';

-- ----------------------------
-- Records of mpds_log
-- ----------------------------

-- ----------------------------
-- Table structure for mpds_mapinfo
-- ----------------------------
DROP TABLE IF EXISTS `mpds_mapinfo`;
CREATE TABLE `mpds_mapinfo` (
  `id` int(11) NOT NULL,
  `send` int(11) DEFAULT NULL COMMENT '发送方账号',
  `devicetype` char(1) DEFAULT NULL COMMENT '终端类型，0 - 对讲机； 1 - 手机',
  `time` datetime DEFAULT NULL COMMENT '信息发送时间',
  `mapinfo` varchar(256) DEFAULT NULL COMMENT '记录事件信息，可报文内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定位信息，只记录最新定位';

-- ----------------------------
-- Records of mpds_mapinfo
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
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8 COMMENT='系统导航菜单';

-- ----------------------------
-- Records of mpds_menu
-- ----------------------------
INSERT INTO `mpds_menu` VALUES ('4', '0', 'System', 'sys_controller', '控制面板', '控制面板', '.icon-setting', '', '203', '0', '系统默认导航，不可更改导航ID', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('7', '4', 'System', 'sys_manager', '系统管理', '', '', '', '150', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('8', '7', 'System', 'manager_list', '用户管理', '', '', 'manager/manager_list.aspx', '99', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('9', '7', 'System', 'manager_role', '角色管理', '', '', 'manager/role_list.aspx', '100', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('10', '19', 'System', 'manager_log', '操作日志', '', '', 'manager/manager_log.aspx', '205', '0', '', 'Show,View,Add,Edit,Delete', '0');
INSERT INTO `mpds_menu` VALUES ('19', '4', 'System', 'sys_business', '业务管理', '', '', '', '202', '0', '', 'Show', '0');
INSERT INTO `mpds_menu` VALUES ('20', '19', 'System', 'bus_distribution_group', '通讯组管理', '', '', 'settings/templet_list.aspx', '99', '0', '', 'Show,View,Add,Edit,Delete,Build', '0');
INSERT INTO `mpds_menu` VALUES ('21', '19', 'System', 'bus_talk_reply', '语言回放', '', '', 'settings/builder_html.aspx', '100', '0', '', 'Show,View,Build', '0');
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

-- ----------------------------
-- Table structure for mpds_message
-- ----------------------------
DROP TABLE IF EXISTS `mpds_message`;
CREATE TABLE `mpds_message` (
  `id` int(11) NOT NULL,
  `send` int(11) DEFAULT NULL COMMENT '发送方账号',
  `receive` int(11) DEFAULT NULL COMMENT '接收方账号，FFFFFFFF表示群发',
  `groupid` int(11) DEFAULT NULL COMMENT '群组编号，关联pdds_group',
  `time` datetime DEFAULT NULL COMMENT '信息发送时间',
  `type` char(1) DEFAULT NULL COMMENT '信息类型，0 - 文本； 1 - 图片； 2 - 语音； 3 - 视频',
  `duration` int(6) DEFAULT NULL COMMENT '如果是语音、小视频，则记录时长',
  `content` varchar(256) DEFAULT NULL COMMENT '如果是文本消息则记录内容，如果是其他则保存路径及文件名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息，包括聊天、图片、语音、视频记录';

-- ----------------------------
-- Records of mpds_message
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='管理角色表';

-- ----------------------------
-- Records of mpds_role
-- ----------------------------
INSERT INTO `mpds_role` VALUES ('1', '超级管理组', '1', '1', '超级管理员，拥有所有权限');
INSERT INTO `mpds_role` VALUES ('3', '后台管理员', '2', '0', '部分管理权限，主要包括后台维护功能');

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
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 COMMENT='管理角色权限表';

-- ----------------------------
-- Records of mpds_role_value
-- ----------------------------
INSERT INTO `mpds_role_value` VALUES ('28', '3', 'sys_controller', 'Show');
INSERT INTO `mpds_role_value` VALUES ('29', '3', 'sys_manager', 'Show');
INSERT INTO `mpds_role_value` VALUES ('30', '3', 'sys_dept', 'Show');
INSERT INTO `mpds_role_value` VALUES ('31', '3', 'manager_list', 'Show');
INSERT INTO `mpds_role_value` VALUES ('32', '3', 'manager_list', 'View');
INSERT INTO `mpds_role_value` VALUES ('33', '3', 'manager_list', 'Add');
INSERT INTO `mpds_role_value` VALUES ('34', '3', 'manager_list', 'Edit');
INSERT INTO `mpds_role_value` VALUES ('35', '3', 'manager_list', 'Delete');
INSERT INTO `mpds_role_value` VALUES ('36', '3', 'manager_role', 'Show');
INSERT INTO `mpds_role_value` VALUES ('37', '3', 'manager_role', 'View');
INSERT INTO `mpds_role_value` VALUES ('38', '3', 'manager_role', 'Add');
INSERT INTO `mpds_role_value` VALUES ('39', '3', 'manager_role', 'Edit');
INSERT INTO `mpds_role_value` VALUES ('40', '3', 'manager_role', 'Delete');
INSERT INTO `mpds_role_value` VALUES ('41', '3', 'sys_navigation', 'Show');
INSERT INTO `mpds_role_value` VALUES ('42', '3', 'sys_navigation', 'View');
INSERT INTO `mpds_role_value` VALUES ('43', '3', 'sys_navigation', 'Add');
INSERT INTO `mpds_role_value` VALUES ('44', '3', 'sys_navigation', 'Edit');
INSERT INTO `mpds_role_value` VALUES ('45', '3', 'sys_navigation', 'Delete');

-- ----------------------------
-- Table structure for mpds_user
-- ----------------------------
DROP TABLE IF EXISTS `mpds_user`;
CREATE TABLE `mpds_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `role_type` int(11) DEFAULT NULL COMMENT '管理员类型1超管2系管',
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '登录密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '6位随机字符串,加密用到',
  `avatar` varchar(255) DEFAULT NULL COMMENT '管理员头像',
  `real_name` varchar(50) DEFAULT NULL COMMENT '用户昵称',
  `telephone` varchar(30) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(30) DEFAULT NULL COMMENT '电子邮箱',
  `is_audit` tinyint(4) DEFAULT NULL,
  `is_lock` int(11) DEFAULT NULL COMMENT '是否锁定',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`id`),
  KEY `role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='管理员信息表';

-- ----------------------------
-- Records of mpds_user
-- ----------------------------
INSERT INTO `mpds_user` VALUES ('1', '1', '1', 'admin', '87FA6AD6CBFDF3108E4DD6F47F5D04A4', '24V0XZ', '', '超级管理员', '13800138000', '', '0', '0', '2017-03-20 13:41:39');
INSERT INTO `mpds_user` VALUES ('2', '1', '1', 'demo', 'B2F2CBE78C4535DE', 'BB4Z4T', '', '', '', '', '0', '0', '2018-08-02 15:11:20');

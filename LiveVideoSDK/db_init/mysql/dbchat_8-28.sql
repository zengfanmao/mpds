/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50620
Source Host           : localhost:3306
Source Database       : dbchat

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2018-08-28 13:22:51
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='组织结构，按树形结构组织';

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
INSERT INTO `mpds_menu` VALUES ('20', '19', 'System', 'sys_group', '通讯组管理', '', '', 'manager/group_list.aspx', '99', '0', '', 'Show,View,Add,Edit,Delete,Build', '0');
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

-- ----------------------------
-- Table structure for tbapitokens
-- ----------------------------
DROP TABLE IF EXISTS `tbapitokens`;
CREATE TABLE `tbapitokens` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `uCode` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `apiToken` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdTime` timestamp NULL DEFAULT NULL,
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbapitokens
-- ----------------------------
INSERT INTO `tbapitokens` VALUES ('2be4f64b-ff7e-4304-bada-acfb0c8670fc', '1001', 'T8WTbiKlVcADow43', '2018-08-22 16:11:53', '0', 0xD58B48EB0608D688);
INSERT INTO `tbapitokens` VALUES ('ac0c1c93-5093-4a6e-8f40-5f3ae42a14fd', '1009', 'g3tRG4CaiHPCJMbX', '2018-08-22 16:12:30', '0', 0xA85D68010708D688);

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
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `appType` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `appVersionNo` int(11) DEFAULT NULL,
  `appVersionName` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `appPublishTime` timestamp NULL DEFAULT NULL,
  `appFeatures` longtext COLLATE utf8mb4_unicode_ci,
  `appTitle` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `appDownloadUrl` varchar(360) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbappversions
-- ----------------------------

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
INSERT INTO `tbcasecollection` VALUES ('BA71CB75-D17D-443D-A7A3-ED42C71E9586', '123                     ', '0', 0x0000000000000C07, '2018-06-25 18:07:08', 'cb9c490b-f9fe-497c-8906-6154d4b2d03c', null, 'wang');

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
INSERT INTO `tbcasedeploys` VALUES ('0', 0x000000000000C614, '1', '1001', null, '522B4E0E-34A0-E811-96FD-509A4C2006E1');
INSERT INTO `tbcasedeploys` VALUES ('0', 0x000000000000C615, '1', '1002', null, '532B4E0E-34A0-E811-96FD-509A4C2006E1');
INSERT INTO `tbcasedeploys` VALUES ('0', 0x000000000000C616, '1', '1003', null, '542B4E0E-34A0-E811-96FD-509A4C2006E1');
INSERT INTO `tbcasedeploys` VALUES ('0', 0x0000000000000D17, '123', 'libai', null, 'E7B9988F-8B77-E811-96F8-509A1C1202E1');
INSERT INTO `tbcasedeploys` VALUES ('0', 0x00000000000007FE, '123', 'baiyuanwei', null, 'E7B9988F-8B77-E811-96F8-509A1C2001E1');
INSERT INTO `tbcasedeploys` VALUES ('0', 0x0000000000000D18, '123', 'dufu', null, 'E7B9988F-8B77-E811-96F8-509A1C2006E1');
INSERT INTO `tbcasedeploys` VALUES ('0', 0x00000000000007DA, '123', 'hexiang', null, 'E7B9988F-8B77-E811-96F8-509A4C2006E1');
INSERT INTO `tbcasedeploys` VALUES ('0', 0x0000000000004C8E, '123', 'meixi', null, 'E7B9988F-8B77-E811-96F8-509A4C3006E1');

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
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `csCode` char(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `msgType` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `msgAbstract` longtext COLLATE utf8mb4_unicode_ci,
  `msgFile` char(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uCode` char(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `msgTime` timestamp NULL DEFAULT NULL,
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `uLatitude` decimal(10,0) DEFAULT NULL,
  `uLongitude` decimal(10,0) DEFAULT NULL,
  `uPositionName` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `msgFromType` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `msgToCode` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbcasemessages
-- ----------------------------

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
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `dCode` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dName` char(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dFather` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dDesc` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbdepartments
-- ----------------------------
INSERT INTO `tbdepartments` VALUES ('0', 0x0000000000000925, '123456', '研发部门', null, null, '9377-E811-96F8-509A4C2006E1');

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
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `createdTime` timestamp NULL DEFAULT NULL,
  `discussionCode` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdUserCode` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdUserName` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uCode` varchar(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csCode` varchar(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbdiscussiongroupmenbers
-- ----------------------------
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('620372E7-96A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E2A3, '2018-08-15 22:24:23', '2', 'hexiang', null, '1001', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('630372E7-96A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E2A4, '2018-08-15 22:24:23', '2', 'hexiang', null, '1002', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('640372E7-96A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E2A5, '2018-08-15 22:24:23', '2', 'hexiang', null, '1003', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('650372E7-96A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E2A6, '2018-08-15 22:24:23', '2', 'hexiang', null, '1004', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('660372E7-96A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E2A7, '2018-08-15 22:24:23', '2', 'hexiang', null, '1005', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('670372E7-96A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E2A8, '2018-08-15 22:24:23', '2', 'hexiang', null, '1006', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('680372E7-96A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E2A9, '2018-08-15 22:24:23', '2', 'hexiang', null, '1007', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('690372E7-96A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E2AA, '2018-08-15 22:24:23', '2', 'hexiang', null, '1008', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('690372E7-96A0-E811-96FE-509A4C2006E2', '0', 0x000000000001406B, '2018-08-15 22:24:23', '3', 'hexiang', null, '1009', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('690372E7-96A0-E811-96FE-509A4C2006E3', '0', 0x000000000001406C, '2018-08-15 22:24:23', '3', 'hexiang', null, '1010', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('80E7E692-95A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E29A, '2018-08-15 22:14:52', '1', 'hexiang', null, '1001', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('81E7E692-95A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E29B, '2018-08-15 22:14:52', '1', 'hexiang', null, '1002', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('82E7E692-95A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E29C, '2018-08-15 22:14:52', '1', 'hexiang', null, '1003', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('83E7E692-95A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E29D, '2018-08-15 22:14:52', '1', 'hexiang', null, '1004', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('84E7E692-95A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E29E, '2018-08-15 22:14:52', '1', 'hexiang', null, '1005', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('85E7E692-95A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E29F, '2018-08-15 22:14:52', '1', 'hexiang', null, '1006', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('86E7E692-95A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E2A0, '2018-08-15 22:14:52', '1', 'hexiang', null, '1007', '');
INSERT INTO `tbdiscussiongroupmenbers` VALUES ('87E7E692-95A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E2A1, '2018-08-15 22:14:52', '1', 'hexiang', null, '1008', '');

-- ----------------------------
-- Table structure for tbdiscussiongroups
-- ----------------------------
DROP TABLE IF EXISTS `tbdiscussiongroups`;
CREATE TABLE `tbdiscussiongroups` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob NOT NULL,
  `createdTime` timestamp NULL DEFAULT NULL,
  `discussionCode` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `discussionName` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdUserCode` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdUserName` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `csCode` varchar(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbdiscussiongroups
-- ----------------------------
INSERT INTO `tbdiscussiongroups` VALUES ('5F4A7B23-95A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E298, '2018-08-15 22:11:45', '1', '交流大会', 'hexiang', null, '');
INSERT INTO `tbdiscussiongroups` VALUES ('610372E7-96A0-E811-96FE-509A4C2006E1', '0', 0x000000000000E2A2, '2018-08-15 22:24:23', '2', '保护首长', 'hexiang', null, '');
INSERT INTO `tbdiscussiongroups` VALUES ('610372E7-96A0-E811-96FE-509A4C2006E3', '0', 0x0000000000014066, '2018-08-15 22:24:23', '3', '开发测试大会', 'hexiang', null, '');

-- ----------------------------
-- Table structure for tbfiledetails
-- ----------------------------
DROP TABLE IF EXISTS `tbfiledetails`;
CREATE TABLE `tbfiledetails` (
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `attRsCode` char(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fCode` char(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fName` char(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fSize` bigint(20) DEFAULT NULL,
  `fAbstract` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fFirstFrame` char(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fStartTime` timestamp NULL DEFAULT NULL,
  `fEndTime` timestamp NULL DEFAULT NULL,
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fRelativePath` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `virtualId` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbfiledetails
-- ----------------------------

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
INSERT INTO `tbunits` VALUES ('A0BDD879-DE76-E811-96F8-109A4C2006E3', '321', '1111', '111', 'xxx单位测试组', 'xxx单位', null, null, null, null, null, null, null, null, null);
INSERT INTO `tbunits` VALUES ('F064F30D-4576-4472-A1CD-CA7D83395D5D', 'A61DEBB3-DF77-4731-942B-0187C931E489', '100', '100', '交流演示', '1001', null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for tbuserimdatas
-- ----------------------------
DROP TABLE IF EXISTS `tbuserimdatas`;
CREATE TABLE `tbuserimdatas` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `LastModifyTime` timestamp NULL DEFAULT NULL,
  `uCode` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
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
-- Records of tbuserimdatas
-- ----------------------------

-- ----------------------------
-- Table structure for tbusers
-- ----------------------------
DROP TABLE IF EXISTS `tbusers`;
CREATE TABLE `tbusers` (
  `isDel` tinyint(1) DEFAULT NULL,
  `recSN` blob,
  `uCode` char(24) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uBelong` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uIsActive` tinyint(1) DEFAULT NULL,
  `rName` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pcNum` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uName` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uSex` int(11) DEFAULT NULL,
  `uDuty` char(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dCode` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uTel` char(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uShortNum` char(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uPassword` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `uHeadPortrait` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dName` char(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LYCID` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `loginFailTimes` smallint(6) DEFAULT NULL,
  `lastLoginTime` timestamp NULL DEFAULT NULL,
  `uRemarks` longtext COLLATE utf8mb4_unicode_ci,
  `Createtime` timestamp NULL DEFAULT NULL,
  `uDepartment` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `accountType` smallint(6) DEFAULT NULL,
  `uEmployeenum` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uIshistory` tinyint(1) DEFAULT NULL,
  `uIsUnilt` tinyint(1) DEFAULT NULL,
  `uIsAccontion` tinyint(1) DEFAULT NULL,
  `uUnitCode` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbusers
-- ----------------------------
INSERT INTO `tbusers` VALUES ('0', 0x0000000000010290, '1001', null, '1', null, null, '陆警官', '0', null, 'A61DEBB3-DF77-4731-942B-0187C931E489', null, null, '06E6C0EA3096DA10229E4B8D9612BAC3', '4A2B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2986382168,4102092451&fm=27&gp=0.jpg', null, null, '0', null, null, null, null, '1', null, null, null, null, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES ('0', 0x0000000000010291, '1002', null, '1', null, null, '王警官', '0', null, 'A61DEBB3-DF77-4731-942B-0187C931E489', null, null, '06E6C0EA3096DA10229E4B8D9612BAC3', '4B2B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1070143562,438881316&fm=27&gp=0.jpg', null, null, '0', null, null, null, null, '1', null, null, null, null, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES ('0', 0x0000000000010292, '1003', null, '1', null, null, '李sir', '0', null, 'A61DEBB3-DF77-4731-942B-0187C931E489', null, null, '06E6C0EA3096DA10229E4B8D9612BAC3', '4C2B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=340995978,612007333&fm=27&gp=0.jpg', null, null, '0', null, null, null, null, '1', null, null, null, null, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES ('0', 0x0000000000010293, '1004', null, '1', null, null, '白sir', '0', null, 'A61DEBB3-DF77-4731-942B-0187C931E489', null, null, '06E6C0EA3096DA10229E4B8D9612BAC3', '4D2B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2634757784,2366771600&fm=27&gp=0.jpg', null, null, '0', null, null, null, null, '1', null, null, null, null, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES ('0', 0x0000000000010294, '1005', null, '1', null, null, '贺sir', '0', null, 'A61DEBB3-DF77-4731-942B-0187C931E489', null, null, '06E6C0EA3096DA10229E4B8D9612BAC3', '4E2B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2783059890,2859384148&fm=27&gp=0.jpg', null, null, '0', null, null, null, null, '1', null, null, null, null, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES ('0', 0x0000000000010295, '1006', null, '1', null, null, '赵sir', '0', null, 'A61DEBB3-DF77-4731-942B-0187C931E489', null, null, '06E6C0EA3096DA10229E4B8D9612BAC3', '4F2B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3994279899,2192014873&fm=200&gp=0.jpg', null, null, '0', null, null, null, null, '1', null, null, null, null, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES ('0', 0x0000000000010296, '1007', null, '1', null, null, '谢sir', '0', null, 'A61DEBB3-DF77-4731-942B-0187C931E489', null, null, '06E6C0EA3096DA10229E4B8D9612BAC3', '502B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1347383990,412756325&fm=27&gp=0.jpg', null, null, '0', null, null, null, null, '1', null, null, null, null, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES ('0', 0x0000000000010297, '1008', null, '1', null, null, '叶sir', '0', null, 'A61DEBB3-DF77-4731-942B-0187C931E489', null, null, '06E6C0EA3096DA10229E4B8D9612BAC3', '512B4E0E-34A0-E811-96FD-509A4C2006E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1178026059,2522313002&fm=27&gp=0.jpgg', null, null, '0', null, null, null, null, '1', null, null, null, null, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES ('0', 0x0000000000014061, '1009', null, '1', null, null, '开发专用1号', '0', null, 'A61DEBB3-DF77-4731-942B-0187C931E489', null, null, '06E6C0EA3096DA10229E4B8D9612BAC3', '512B4E0E-34A0-E811-96FD-509A4C2006E2', 'http://img5.imgtn.bdimg.com/it/u=2832797993,2676010642&fm=27&gp=0.jpg', null, null, '0', null, null, null, null, '1', null, null, null, null, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES ('0', 0x0000000000014070, '1010', null, '1', null, null, '开发专用2号', '0', null, 'A61DEBB3-DF77-4731-942B-0187C931E489', null, null, '06E6C0EA3096DA10229E4B8D9612BAC3', '512B4E0E-34A0-E811-96FD-509A4C2006E3', 'http://img1.imgtn.bdimg.com/it/u=3402051892,3248047759&fm=27&gp=0.jpg', null, null, '0', null, null, null, null, '1', null, null, null, null, 'A61DEBB3-DF77-4731-942B-0187C931E489');
INSERT INTO `tbusers` VALUES ('0', 0x000000000001028E, 'baiyuanwei', null, '1', null, null, '白垣伟', '0', '武警', '123456', '4444', null, 'E10ADC3949BA59ABBE56E057F20F883E', '79CDCF99-8C77-E811-96F8-509A4C2006E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2986382168,4102092451&fm=27&gp=0.jpg', null, null, '0', null, null, null, null, '1', null, null, null, null, '321');
INSERT INTO `tbusers` VALUES ('0', 0x000000000001028A, 'hexiang', null, '1', null, null, '贺祥', '0', '警察', '123456', null, null, 'E10ADC3949BA59ABBE56E057F20F883E', 'A0BDD879-DE76-E811-96F8-109A4C2006E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2986382168,4102092451&fm=27&gp=0.jpg', null, null, '0', null, null, null, null, '1', null, null, null, null, '321');
INSERT INTO `tbusers` VALUES ('0', 0x000000000001028B, 'meixi', null, '1', null, null, '梅西', '0', '警察', '123456', null, null, 'E10ADC3949BA59ABBE56E057F20F883E', 'A0BDD879-DE76-E811-96F8-109A4C2106E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2986382168,4102092451&fm=27&gp=0.jpg', null, null, '0', null, null, null, null, '1', null, null, null, null, '321');
INSERT INTO `tbusers` VALUES ('0', 0x000000000001028C, 'dufu', null, '1', null, null, '杜甫', '0', '警察', '123456', null, null, 'E10ADC3949BA59ABBE56E057F20F883E', 'A0BDD879-DE76-E811-96F8-109A4C3006E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2986382168,4102092451&fm=27&gp=0.jpg', null, null, '0', null, null, null, null, '1', null, null, null, null, '321');
INSERT INTO `tbusers` VALUES ('0', 0x000000000001028D, 'libai', null, '1', null, null, '李白', '0', '警察', '123456', null, null, 'E10ADC3949BA59ABBE56E057F20F883E', 'A0BDD879-DE76-E811-96F8-509A4C2006E1', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2986382168,4102092451&fm=27&gp=0.jpg', null, null, '0', null, null, null, null, '1', null, null, null, null, '321');

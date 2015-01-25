/*
Navicat MySQL Data Transfer

Source Server         : znz
Source Server Version : 50525
Source Host           : localhost:3306
Source Database       : znz

Target Server Type    : MYSQL
Target Server Version : 50525
File Encoding         : 65001

Date: 2015-01-26 00:52:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(64) NOT NULL,
  `pwd` varchar(16) NOT NULL,
  `company` varchar(64) NOT NULL,
  `limit_ip_flag` int(11) NOT NULL,
  `limit_ips` varchar(120) NOT NULL,
  `access_flag` int(11) NOT NULL,
  `max_download_times` int(11) NOT NULL,
  `download_per_day` int(11) NOT NULL,
  `download_total` int(11) NOT NULL,
  `phone` varchar(32) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `session_id` varchar(32) DEFAULT NULL,
  `user_type` int(11) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'ht', '123', '翼支付', '0', '127.0.0.1', '1', '0', '1', '1', '1', '2015-01-25 03:17:43', '2015-01-25 21:24:13', 'jt0pif2qfe9j154opj9asafya', '2');
INSERT INTO `t_user` VALUES ('3', 'test2', 'test', '意尔康2', '0', '127.0.0.1', '0', '0', '0', '0', null, '2015-01-25 02:54:45', '2015-01-13 02:54:42', null, '1');
INSERT INTO `t_user` VALUES ('4', 'test3', 'test', '意尔康4', '0', '127.0.0.1', '0', '0', '0', '0', null, '2015-01-25 02:54:48', '2015-01-07 02:54:46', null, '1');
INSERT INTO `t_user` VALUES ('5', 'test4', 'test', '意尔康3', '0', '127.0.0.1', '0', '0', '0', '0', null, '2015-01-25 02:54:51', '2015-01-21 02:54:49', null, '1');
INSERT INTO `t_user` VALUES ('6', 'test6', 'test', '意尔康5', '0', '', '0', '0', '0', '0', null, '2015-01-25 02:55:00', '2015-01-15 02:54:56', null, '1');
INSERT INTO `t_user` VALUES ('7', 'huangtao', '123456', '测试', '1', '', '1', '10', '0', '0', null, '2015-01-25 04:51:03', '2015-01-25 04:51:03', null, '1');
INSERT INTO `t_user` VALUES ('8', 'ht123', '123456', '测试', '1', '', '1', '12', '0', '0', null, '2015-01-25 04:52:24', '2015-01-25 04:52:24', null, '1');
INSERT INTO `t_user` VALUES ('9', 'ht1234', '123456', '公司名称', '1', '', '1', '123', '0', '0', null, '2015-01-25 05:06:06', '2015-01-25 05:06:06', null, '1');
INSERT INTO `t_user` VALUES ('10', 'ht1111', '123456', '而特232', '1', '', '1', '1111', '0', '0', null, '2015-01-25 05:11:26', '2015-01-25 05:11:26', null, '1');
INSERT INTO `t_user` VALUES ('11', 'ht123445', '123456', '顶顶顶顶', '1', '', '1', '1111', '0', '0', null, '2015-01-25 05:14:57', '2015-01-25 05:14:57', null, '1');
INSERT INTO `t_user` VALUES ('12', 'ht1567', '123456', '天天二二', '1', '', '1', '1111', '0', '0', null, '2015-01-25 05:16:50', '2015-01-25 05:16:50', null, '1');
INSERT INTO `t_user` VALUES ('13', 'ht1', '111', '111', '1', '11', '1', '1111', '0', '0', null, '2015-01-25 05:18:51', '2015-01-25 05:18:51', null, '1');
INSERT INTO `t_user` VALUES ('14', '5555', '5555', '5555改', '1', '', '1', '5555', '0', '0', null, '2015-01-25 19:30:32', '2015-01-25 19:30:30', null, '1');
INSERT INTO `t_user` VALUES ('15', '111111', '1111111', 'IBM', '1', '127.0.0.1', '0', '9', '0', '0', null, '2015-01-25 22:24:29', '2015-01-25 22:24:27', null, '1');
INSERT INTO `t_user` VALUES ('16', 'ht543', '1111', 'test', '0', '', '0', '11', '0', '0', null, '2015-01-25 23:19:57', '2015-01-25 23:19:57', null, '1');
INSERT INTO `t_user` VALUES ('17', '1', '1', 'IBM2', '0', '1', '0', '1', '0', '0', null, '2015-01-25 23:24:03', '2015-01-25 23:24:03', null, '1');
INSERT INTO `t_user` VALUES ('18', '---', '00', 'pp', '0', '', '0', '9', '0', '0', null, '2015-01-25 23:29:11', '2015-01-25 23:29:11', null, '1');
INSERT INTO `t_user` VALUES ('19', '2', '2', '2', '0', '', '0', '2222', '0', '0', null, '2015-01-25 23:36:49', '2015-01-25 23:36:49', null, '1');
INSERT INTO `t_user` VALUES ('20', '3', '3', '3', '0', '', '0', '3', '0', '0', null, '2015-01-25 23:38:08', '2015-01-25 23:38:08', null, '1');
INSERT INTO `t_user` VALUES ('21', '4', '4', '4', '0', '4', '0', '4', '0', '0', null, '2015-01-25 23:40:57', '2015-01-25 23:40:55', null, '1');
INSERT INTO `t_user` VALUES ('22', 'ERERER', '1', '11111', '0', '1', '0', '1111', '0', '0', null, '2015-01-26 00:31:18', '2015-01-26 00:31:17', null, '1');

-- ----------------------------
-- Table structure for `t_user_auth`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_auth`;
CREATE TABLE `t_user_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `file_path` varchar(240) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='用户访问权限表';

-- ----------------------------
-- Records of t_user_auth
-- ----------------------------
INSERT INTO `t_user_auth` VALUES ('4', '15', '国外女鞋', '2015-01-25 22:24:29', '2015-01-25 22:24:29');
INSERT INTO `t_user_auth` VALUES ('5', '1', '国内女鞋', '2015-01-25 23:22:29', '2015-01-25 23:22:29');
INSERT INTO `t_user_auth` VALUES ('7', '21', '国外女鞋', '2015-01-25 23:40:57', '2015-01-25 23:40:57');
INSERT INTO `t_user_auth` VALUES ('11', '22', '国外女鞋', '2015-01-26 00:31:18', '2015-01-26 00:31:18');

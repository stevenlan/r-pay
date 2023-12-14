# ************************************************************
# Sequel Ace SQL dump
# 版本号： 20057
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# 主机: 127.0.0.1 (MySQL 8.0.21)
# 数据库: rpay
# 生成时间: 2023-12-14 12:22:32 +0000
# ************************************************************

use rpay;
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# 转储表 account_policy
# ------------------------------------------------------------

DROP TABLE IF EXISTS `account_policy`;

CREATE TABLE `account_policy` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `provider_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `esp_price` decimal(10,5) NOT NULL,
  `min_value` decimal(10,5) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 balance_detail
# ------------------------------------------------------------

DROP TABLE IF EXISTS `balance_detail`;

CREATE TABLE `balance_detail` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `coin_code` varchar(16) NOT NULL COMMENT '余额货币代码',
  `balance` double(16,4) NOT NULL COMMENT '余额',
  `bal_type` int DEFAULT NULL COMMENT '账本类型，1 法币 2 加密',
  `user_id` int NOT NULL COMMENT '所属用户',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 bank_detail
# ------------------------------------------------------------

DROP TABLE IF EXISTS `bank_detail`;

CREATE TABLE `bank_detail` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `account_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '账户名称',
  `country` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `account_add` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `bank_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `swift_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `bank_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `bank_account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `bank_country` varchar(16) DEFAULT NULL,
  `bank_add` varchar(128) DEFAULT NULL,
  `account_cer` varchar(64) DEFAULT NULL COMMENT '银行资料证明',
  `user_id` int NOT NULL,
  `bank_status` int NOT NULL DEFAULT '0' COMMENT '0 未审核 1已通过 2 驳回',
  `reason` varchar(128) DEFAULT NULL COMMENT '审核备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `bank_status` (`bank_status`),
  KEY `modified_time` (`modified_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 bill_detail
# ------------------------------------------------------------

DROP TABLE IF EXISTS `bill_detail`;

CREATE TABLE `bill_detail` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `bill_type` int NOT NULL COMMENT '账单类型 1 入账，2 出账 3 换汇 4 换汇的入账',
  `coin_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单货币代码',
  `bill_value` double(16,4) NOT NULL COMMENT '账单金额',
  `outer_type` int DEFAULT NULL COMMENT '单据类型 1 法币，2 加密货币',
  `commission` double(12,4) DEFAULT NULL COMMENT '手续费',
  `balance` double(16,4) DEFAULT NULL COMMENT '当前余额',
  `user_id` int NOT NULL COMMENT '账单所属用户',
  `recorded` int DEFAULT NULL,
  `outer_id` int NOT NULL COMMENT '关联业务id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 change_detail
# ------------------------------------------------------------

DROP TABLE IF EXISTS `change_detail`;

CREATE TABLE `change_detail` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `dep_coin` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '兑换货币代码',
  `target_coin` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '兑换目标货币代码',
  `dep_value` double(16,4) NOT NULL COMMENT '兑换金额',
  `target_value` double(16,4) NOT NULL COMMENT '兑换目标金额',
  `change_rate` float(13,4) NOT NULL COMMENT '汇率',
  `balance` float(13,4) DEFAULT NULL COMMENT '差值',
  `change_status` int NOT NULL COMMENT '状态 0 未确认 1已确认 2已取消 3 已驳回',
  `user_id` int NOT NULL,
  `recorded` int DEFAULT NULL,
  `out_id` int DEFAULT NULL,
  `memo` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 cms_contact
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cms_contact`;

CREATE TABLE `cms_contact` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `email` varchar(128) DEFAULT NULL,
  `phone` varchar(32) DEFAULT NULL,
  `country` varchar(32) DEFAULT NULL,
  `biz_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `content` text,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 cry_account
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cry_account`;

CREATE TABLE `cry_account` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `cry_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '加密货币代号',
  `cry_name` varchar(32) DEFAULT NULL COMMENT '钱包别名',
  `agreement` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `cry_add` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密货币钱包地址',
  `user_id` int NOT NULL COMMENT '关联用户',
  `cry_type` int NOT NULL DEFAULT '0' COMMENT '0 平台收款地址 1 用户提款地址',
  `acc_status` int DEFAULT NULL COMMENT '1 使用中 2 失效',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 crypt_req
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crypt_req`;

CREATE TABLE `crypt_req` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `coin_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `src_code` varchar(16) DEFAULT NULL,
  `req_value` double(16,4) NOT NULL,
  `wit_value` double DEFAULT NULL,
  `agreement` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `crypt_add` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `tid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `req_type` int NOT NULL COMMENT '申请类型 1 入金 2 出金',
  `req_status` int NOT NULL DEFAULT '1' COMMENT '若是入金 1 已提交 2 已入账 若是出金 1 已提交 2 已出账',
  `req_proof` varchar(128) DEFAULT NULL,
  `user_id` int NOT NULL,
  `recorded` int DEFAULT NULL,
  `memo` varchar(128) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 dep_request
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dep_request`;

CREATE TABLE `dep_request` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `coin_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '入金货币代码',
  `bank_id` int NOT NULL COMMENT '入金收款银行账户',
  `send_bank` int NOT NULL COMMENT '入金打款银行账户',
  `account_name` varchar(64) DEFAULT NULL,
  `send_account` varchar(64) DEFAULT NULL,
  `req_value` double(16,4) NOT NULL COMMENT '申请入金金额',
  `dep_value` double(16,4) DEFAULT NULL COMMENT '实际入金金额',
  `user_id` int NOT NULL COMMENT '入金用户',
  `recorded` int DEFAULT NULL COMMENT '核算审核人',
  `req_status` int DEFAULT NULL COMMENT '申请状态 1 提交申请 2 确认汇款 3 财务审核 4 入账完成',
  `req_proof` varchar(128) DEFAULT NULL COMMENT '入金凭证上传文件(转账流水)，在确认汇款时提交',
  `memo` varchar(64) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 file_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `file_info`;

CREATE TABLE `file_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL COMMENT '资源路径',
  `name` varchar(128) DEFAULT NULL COMMENT '资源原始名称',
  `file_name` varchar(128) DEFAULT NULL COMMENT '资源名称',
  `suffix` varchar(20) DEFAULT NULL COMMENT '后缀名',
  `is_img` tinyint(1) DEFAULT NULL COMMENT '是否图片',
  `size` int DEFAULT NULL COMMENT '尺寸',
  `type` varchar(10) DEFAULT NULL COMMENT '文件展示类型，非后缀名',
  `put_time` datetime DEFAULT NULL COMMENT '上传时间',
  `is_dir` tinyint(1) DEFAULT NULL COMMENT '是否目录',
  `parent_id` int DEFAULT NULL,
  `source` varchar(10) DEFAULT NULL COMMENT '来源',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件资源表';



# 转储表 kyc_certification
# ------------------------------------------------------------

DROP TABLE IF EXISTS `kyc_certification`;

CREATE TABLE `kyc_certification` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `company_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '企业名称',
  `company_en_name` varchar(64) DEFAULT NULL COMMENT '企业英文名称',
  `business_add` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '经营地址',
  `country` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '国家',
  `reg_date` varchar(16) DEFAULT NULL COMMENT '注册日期',
  `bus_type` int NOT NULL COMMENT '1 个体工商户 2 企业 3 海外公司',
  `period` varchar(16) DEFAULT NULL COMMENT '企业有效期',
  `monthly_remittance` varchar(16) DEFAULT NULL COMMENT '月汇款金额',
  `transactions_month` varchar(16) DEFAULT NULL COMMENT '月交易笔数',
  `transaction_limit` varchar(16) DEFAULT NULL COMMENT '单笔额度',
  `business_scenario` varchar(128) DEFAULT NULL COMMENT '业务场景',
  `web_site` varchar(64) DEFAULT NULL COMMENT '企业官网',
  `reg_cer` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '企业资料信息',
  `legal` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '法人信息',
  `shareholder` varchar(64) DEFAULT NULL COMMENT '股东结构及信息',
  `user_id` int NOT NULL,
  `kyc_status` int NOT NULL DEFAULT '0' COMMENT '0未审核 1 已通过 2 驳回',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `reason` varchar(256) DEFAULT NULL COMMENT '审核意见',
  PRIMARY KEY (`id`),
  KEY `modified_time` (`modified_time`),
  KEY `kyc_status` (`kyc_status`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 kyc_detail
# ------------------------------------------------------------

DROP TABLE IF EXISTS `kyc_detail`;

CREATE TABLE `kyc_detail` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `provider_id` varchar(16) NOT NULL DEFAULT '',
  `kyc_bank` json DEFAULT NULL,
  `kyc_info` json DEFAULT NULL,
  `kyc_attachment` json DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `reason` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `provider_id` (`provider_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 site_news
# ------------------------------------------------------------

DROP TABLE IF EXISTS `site_news`;

CREATE TABLE `site_news` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL,
  `lang` varchar(16) DEFAULT 'zh',
  `cover` varchar(128) NOT NULL,
  `main_point` varchar(256) NOT NULL,
  `content` text NOT NULL,
  `new_status` int NOT NULL DEFAULT '0',
  `recommend` int NOT NULL,
  `count` int DEFAULT '0',
  `author` varchar(32) DEFAULT NULL,
  `user_id` int NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 sys_auth
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_auth`;

CREATE TABLE `sys_auth` (
  `id` int NOT NULL AUTO_INCREMENT,
  `auth_code` varchar(30) DEFAULT NULL,
  `auth_name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限表';

LOCK TABLES `sys_auth` WRITE;
/*!40000 ALTER TABLE `sys_auth` DISABLE KEYS */;

INSERT INTO `sys_auth` (`id`, `auth_code`, `auth_name`)
VALUES
	(1,'file:view','查看文件'),
	(2,'file:copy','拷贝文件'),
	(3,'file:move','移动文件'),
	(4,'file:rename','重命名文件'),
	(5,'file:download','下载文件'),
	(6,'file:delete','删除文件'),
	(7,'file:upload','上传文件'),
	(8,'dir:manage','目录管理'),
	(9,'dir:add','创建目录');

/*!40000 ALTER TABLE `sys_auth` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 sys_countries
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_countries`;

CREATE TABLE `sys_countries` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '中文名字',
  `code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '简称',
  `en_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '英文名字',
  `area_code` varchar(16) DEFAULT NULL,
  `coin_code` varchar(16) DEFAULT NULL,
  `coin_type` int DEFAULT '1' COMMENT '1 法币 2 加密',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sys_countries` WRITE;
/*!40000 ALTER TABLE `sys_countries` DISABLE KEYS */;

INSERT INTO `sys_countries` (`id`, `name`, `code`, `en_name`, `area_code`, `coin_code`, `coin_type`)
VALUES
	(1,'阿尔巴尼亚','AL','Albania','+355','ALL',1),
	(2,'阿尔及利亚','DZ','Algeria','+213','DZD',1),
	(3,'阿富汗','AF','Afghanistan','+93','AFN',1),
	(4,'阿根廷','AR','Argentina','+54','ARS',1),
	(5,'阿联酋','AE','United Arab Emirates','+971','AED',1),
	(6,'阿鲁巴岛','AW','Aruba','+297','AWG',1),
	(7,'阿曼','OM','Oman','+968','OMR',1),
	(8,'阿塞拜疆','AZ','Azerbaijan','+994','AZN',1),
	(9,'埃及','EG','Egypt','+20','EGP',1),
	(10,'埃塞俄比亚','ET','Ethiopia','+251','ETB',1),
	(11,'爱尔兰','IE','Ireland','+353','EUR',1),
	(12,'爱沙尼亚','EE','Estonia','+372','EUR',1),
	(13,'安道尔','AD','Andorra','+376','ADP',1),
	(14,'安哥拉','AO','Angola','+244','AOA',1),
	(15,'安圭拉','AI','Anguilla','+1809','XCD',1),
	(16,'安提瓜和巴布达','AG','Antigua and Barbuda','+1268','XCU',1),
	(17,'奥地利','AT','Austria','+43','EUR',1),
	(18,'奥兰群岛','AX','Aland Islands',NULL,NULL,1),
	(19,'澳大利亚','AU','Australia','+61','AUD',1),
	(20,'梵蒂冈','VA','Holy See','+396','EUR',1),
	(21,'巴巴多斯','BB','Barbados','+1809','BBD',1),
	(22,'巴布亚新几内亚','PG','Papua New Guinea','+675','PGK',1),
	(23,'巴哈马','BS','Bahamas, The','+1809','BSD',1),
	(24,'巴基斯坦','PK','Pakistan','+92','PKR',1),
	(25,'巴拉圭','PY','Paraguay','+595','PYG',1),
	(26,'巴勒斯坦','PS','Palestinian Territories','+970',NULL,1),
	(27,'巴林','BH','Bahrain','+973','BHD',1),
	(28,'巴拿马','PA','Panama','+507','PAB',1),
	(29,'巴西','BR','Brazil','+55','BRL',1),
	(30,'白俄罗斯','BY','Belarus','+375','BYR',1),
	(31,'百慕大','BM','Bermuda','+1441','BMD',1),
	(32,'保加利亚','BG','Bulgaria','+359','BGN',1),
	(33,'北马里亚纳群岛','MP','Northern Mariana Islands','+1670',NULL,1),
	(34,'贝宁','BJ','Benin','+229',NULL,1),
	(35,'比利时','BE','Belgium','+32','EUR',1),
	(36,'冰岛','IS','Iceland','+354','ISK',1),
	(37,'玻利维亚','BO','Bolivia','+591','BOB',1),
	(38,'波多黎各','PR','Puerto Rico','+1809','USD',1),
	(39,'波黑','BA','Bosnia and Herzegovina','+387','BAM',1),
	(40,'波兰','PL','Poland','+48','PLN',1),
	(41,'博茨瓦纳','BW','Botswana','+267','BWP',1),
	(42,'博奈尔岛、圣尤斯特歇、萨巴','BQ','Bonaire, Saint Eustatius and Saba',NULL,NULL,1),
	(43,'伯利兹','BZ','Belize','+501','BZD',1),
	(44,'不丹','BT','Bhutan','+975','BTN',1),
	(45,'布基纳法索','BF','Burkina Faso','+226',NULL,1),
	(46,'布隆迪','BI','Burundi','+257','BIF',1),
	(47,'布维岛','BV','Bouvet Island',NULL,NULL,1),
	(48,'赤道几内亚','GQ','Equatorial Guinea','+240',NULL,1),
	(49,'丹麦','DK','Denmark','+45','DKK',1),
	(50,'德国','DE','Germany','+349','EUR',1),
	(51,'东帝汶','TL','Timor-leste','+670','USD',1),
	(52,'多哥','TG','Togo','+228','XOF',1),
	(53,'多米尼加','DM','Dominica','+1809','DOP',1),
	(54,'多米尼加共和国','DO','Dominican Republic','+1809','DOP',1),
	(55,'俄罗斯','RU','Russian Federation','+7','RUB',1),
	(56,'厄瓜多尔','EC','Ecuador','+593','USD',1),
	(57,'厄立特里亚','ER','Eritrea','+291','ERN',1),
	(58,'法国','FR','France','+33','EUR',1),
	(59,'法罗群岛','FO','Faroe Islands','+298',NULL,1),
	(60,'法属波利尼西亚','PF','French Polynesia','+689',NULL,1),
	(61,'法属圭亚那','GF','French Guiana','+594',NULL,1),
	(62,'法属南部领土','TF','French Southern Territories',NULL,NULL,1),
	(63,'菲律宾','PH','Philippines','+63','PHP',1),
	(64,'芬兰','FI','Finland','+358','EUR',1),
	(65,'佛得角','CV','Cape Verde','+238','CVE',1),
	(66,'福克兰群岛（马尔维纳斯）','FK','Falkland Islands (Malvinas)','+500','FKP',1),
	(67,'冈比亚','GM','Gambia, The','+220','GMD',1),
	(68,'刚果','CG','Congo','+242','CDF',1),
	(69,'刚果（金）','CD','Congo, The Democratic Republic of the','+242','CDF',1),
	(70,'哥伦比亚','CO','Colombia','+57','COP',1),
	(71,'哥斯达黎加','CR','Costa Rica','+506','CRC',1),
	(72,'格恩西岛','GG','Guernsey',NULL,NULL,1),
	(73,'格林纳达','GD','Grenada','+1473','XCD',1),
	(74,'格陵兰岛','GL','Greenland','+299',NULL,1),
	(75,'格鲁吉亚','GE','Georgia','+995','GEL',1),
	(76,'瓜德罗普岛','GP','Guadeloupe','+590',NULL,1),
	(77,'关岛','GU','Guam','+671','USD',1),
	(78,'圭亚那','GY','Guyana','+592','GYD',1),
	(79,'哈萨克斯坦','KZ','Kazakhstan','+7','KZT',1),
	(80,'海地','HT','Haiti','+509','HTG',1),
	(81,'韩国','KR','Korea, Republic of','+82','KRW',1),
	(82,'荷兰','NL','Netherlands','+31','EUR',1),
	(83,'荷属安的列斯','AN','Netherlands Antilles','+599','ANG',1),
	(84,'赫德岛和麦克唐纳群岛','HM','Heard Island and the McDonald Islands',NULL,NULL,1),
	(85,'黑山共和国','ME','Montenegro','+382','EUR',1),
	(86,'洪都拉斯','HN','Honduras','+504','HNL',1),
	(87,'基里巴斯','KI','Kiribati','+686',NULL,1),
	(88,'吉布提','DJ','Djibouti','+253','DJF',1),
	(89,'吉尔吉斯坦','KG','Kyrgyzstan','+996','KGS',1),
	(90,'几内亚','GN','Guinea','+224','GNF',1),
	(91,'几内亚比绍','GW','Guinea-Bissau','+245',NULL,1),
	(92,'加拿大','CA','Canada','+1','CAD',1),
	(93,'加纳','GH','Ghana','+233','GHS',1),
	(94,'加蓬','GA','Gabon','+241',NULL,1),
	(95,'柬埔寨','KH','Cambodia','+855','KHR',1),
	(96,'捷克共和国','CZ','Czech Republic','+420','CZK',1),
	(97,'津巴布韦','ZW','Zimbabwe','+263',NULL,1),
	(98,'喀麦隆','CM','Cameroon','+237',NULL,1),
	(99,'卡塔尔','QA','Qatar','+974','QAR',1),
	(100,'开曼群岛','KY','Cayman Islands','+1','KYD',1),
	(101,'科科斯（基林）群岛','CC','Cocos (Keeling) Islands','+61891',NULL,1),
	(102,'科摩罗','KM','Comoros','+269','KMF',1),
	(103,'科索沃','XK','Kosovo','+381',NULL,1),
	(104,'科特迪瓦','CI','Cote D&#39;ivoire','+225',NULL,1),
	(105,'科威特','KW','Kuwait','+965','KWD',1),
	(106,'克罗地亚','HR','Croatia','+385','EUR',1),
	(107,'肯尼亚','KE','Kenya','+254','KES',1),
	(108,'库克群岛','CK','Cook Islands','+682',NULL,1),
	(109,'库拉索岛','CW','Curaçao','+5999',NULL,1),
	(110,'拉脱维亚','LV','Latvia','+371','EUR',1),
	(111,'莱索托','LS','Lesotho','+266','LSL',1),
	(112,'老挝','LA','Lao People&#39;s Democratic Republic','+856','LAK',1),
	(113,'黎巴嫩','LB','Lebanon','+961','LBP',1),
	(114,'利比里亚','LR','Liberia','+231','LRD',1),
	(115,'利比亚','LY','Libya','+218','LYD',1),
	(116,'立陶宛','LT','Lithuania','+370','EUR',1),
	(117,'列支敦士登','LI','Liechtenstein','+4175','CHF',1),
	(118,'留尼旺岛','RE','Reunion','+262','EUR',1),
	(119,'卢森堡','LU','Luxembourg','+352','EUR',1),
	(120,'卢旺达','RW','Rwanda','+250','RWF',1),
	(121,'罗马尼亚','RO','Romania','+40','RON',1),
	(122,'马达加斯加','MG','Madagascar','+261','MGA',1),
	(123,'马恩岛','IM','Isle of Man',NULL,NULL,1),
	(124,'马耳他','MT','Malta','+356','EUR',1),
	(125,'马尔代夫','MV','Maldives','+960','MBR',1),
	(126,'马拉维','MW','Malawi','+265','MWK',1),
	(127,'马来西亚','MY','Malaysia','+60','MYR',1),
	(128,'马里','ML','Mali','+223','XOF',1),
	(129,'马其顿','MK','acedonia, The Former Yugoslav Republic of','+389','MKD',1),
	(130,'马绍尔群岛','MH','Marshall Islands','+692','USD',1),
	(131,'马提尼克岛','MQ','Martinique','+596','EUR',1),
	(132,'马约特岛','YT','Mayotte','+269','EUR',1),
	(133,'毛里求斯','MU','Mauritius','+230','MUR',1),
	(134,'毛里塔尼亚','MR','Mauritania','+222','MRO',1),
	(135,'美国','US','United States','+1','USD',1),
	(136,'美国本土外小岛屿','UM','United States Minor Outlying Islands',NULL,NULL,1),
	(137,'美属萨摩亚','AS','American Samoa','+685',NULL,1),
	(138,'美属维尔京群岛','VI','Virgin Islands, U.S.',NULL,NULL,1),
	(139,'蒙古','MN','Mongolia','+976','MNT',1),
	(140,'蒙特塞拉特岛','MS','Montserrat','+1664','XCD',1),
	(141,'孟加拉国','BD','Bangladesh','+880','BDT',1),
	(142,'秘鲁','PE','Peru','+51','PEN',1),
	(143,'密克罗尼西亚','FM','Micronesia, Federated States of','+691','USD',1),
	(144,'缅甸','MM','Myanmar','+95','MMK',1),
	(145,'摩尔多瓦','MD','Moldova, Republic of','+373','MDL',1),
	(146,'摩洛哥','MA','Morocco','+210','MAD',1),
	(147,'摩纳哥','MC','Monaco','+377','EUR',1),
	(148,'莫桑比克','MZ','Mozambique','+258','MZN',1),
	(149,'墨西哥','MX','Mexico','+52','MXN',1),
	(150,'纳米比亚','NA','Namibia','+264','NAD',1),
	(151,'南非','ZA','South Africa','+27','ZAR',1),
	(152,'南极洲','AQ','Antarctica',NULL,NULL,1),
	(153,'南乔治亚和南桑德韦奇群岛','GS','South Georgia and the South Sandwich Islands',NULL,NULL,1),
	(154,'尼泊尔','NP','Nepal','+977','NPR',1),
	(155,'尼加拉瓜','NI','Nicaragua','+505','NIO',1),
	(156,'尼日尔','NE','Niger','+227',NULL,1),
	(157,'尼日利亚','NG','Nigeria','+234','NGN',1),
	(158,'纽埃岛','NU','Niue','+683',NULL,1),
	(159,'挪威','NO','Norway','+47','NOK',1),
	(160,'诺福克岛','NF','Norfolk Island','+6723',NULL,1),
	(161,'帕劳群岛','PW','Palau','+680','USD',1),
	(162,'皮特凯恩','PN','Pitcairn','+64',NULL,1),
	(163,'葡萄牙','PT','Portugal','+351','EUR',1),
	(164,'日本','JP','Japan','+81','JPY',1),
	(165,'瑞典','SE','Sweden','+46','SEK',1),
	(166,'瑞士','CH','Switzerland','+41','CHF',1),
	(167,'萨尔瓦多','SV','El Salvador','+503',NULL,1),
	(168,'萨摩亚群岛','WS','Samoa','+685','WST',1),
	(169,'塞尔维亚','RS','Serbia','+381','RSD',1),
	(170,'塞拉利昂','SL','Sierra Leone','+232','SLL',1),
	(171,'塞内加尔','SN','Senegal','+221',NULL,1),
	(172,'塞浦路斯','CY','Cyprus','+357','EUR',1),
	(173,'塞舌尔','SC','Seychelles','+248','SCR',1),
	(174,'沙特阿拉伯','SA','Saudi Arabia','+966','SAR',1),
	(175,'圣巴泰勒米','BL','Saint Barthelemy','+590','EUR',1),
	(176,'圣诞岛','CX','Christmas Island','+6724',NULL,1),
	(177,'圣多美和普林西比','ST','Sao Tome and Principe','+239','STD',1),
	(178,'圣赫勒拿、阿森松与特里斯坦达库尼亚','SH','Saint Helena, Ascension and Tristan da Cunha','+290',NULL,1),
	(179,'圣基茨和尼维斯','KN','Saint Kitts and Nevis','+1869',NULL,1),
	(180,'圣卢西亚','LC','Saint Lucia','+1809',NULL,1),
	(181,'圣马丁','MF','Saint Martin',NULL,NULL,1),
	(182,'圣马丁岛','SX','Sint Maarten',NULL,NULL,1),
	(183,'圣马力诺','SM','San Marino','+223','EUR',1),
	(184,'圣皮埃尔和密克隆岛','PM','Saint Pierre and Miquelon',NULL,NULL,1),
	(185,'圣文森特和格林纳丁斯群岛','VC','Saint Vincent and the Grenadines',NULL,NULL,1),
	(186,'斯里兰卡','LK','Sri Lanka','+94','LKR',1),
	(187,'斯洛伐克','SK','Slovakia','+421','EUR',1),
	(188,'斯洛文尼亚','SI','Slovenia','+386','EUR',1),
	(189,'斯瓦尔巴岛和扬马延岛','SJ','Svalbard and Jan Mayen',NULL,NULL,1),
	(190,'斯威士兰','SZ','Swaziland','+268','SZL',1),
	(191,'苏里南','SR','Suriname','+597','SRD',1),
	(192,'索马里','SO','Somalia','+252','SOS',1),
	(193,'所罗门群岛','SB','Solomon Islands','+677','SBD',1),
	(194,'塔吉克斯坦','TJ','Tajikistan',NULL,'TJS',1),
	(195,'瑙鲁','NR','Nauru','+674',NULL,1),
	(196,'泰国','TH','Thailand','+66','THB',1),
	(197,'坦桑尼亚','TZ','Tanzania, United Republic of','+255','TZS',1),
	(198,'汤加','TO','Tonga','+676','TOP',1),
	(199,'特克斯和凯科斯群岛','TC','Turks and Caicos Islands',NULL,NULL,1),
	(200,'特立尼达和多巴哥','TT','Trinidad and Tobago',NULL,NULL,1),
	(201,'突尼斯','TN','Tunisia','+216','TND',1),
	(202,'图瓦卢','TV','Tuvalu','+688','TVD',1),
	(203,'土耳其','TR','Turkey','+90','TRY',1),
	(204,'土库曼斯坦','TM','Turkmenistan','+993','TMT',1),
	(205,'托克劳群岛','TK','Tokelau','+690',NULL,1),
	(206,'瓦利斯群岛和富图纳群岛','WF','Wallis and Futuna',NULL,NULL,1),
	(207,'瓦努阿图','VU','Vanuatu','+678','VUV',1),
	(208,'危地马拉','GT','Guatemala','+502','GTQ',1),
	(209,'委内瑞拉','VE','Venezuela','+58','VES',1),
	(210,'文莱','BN','Brunei Darussalam','+673','BND',1),
	(211,'乌干达','UG','Uganda','+256','UGX',1),
	(212,'乌克兰','UA','Ukraine','+380','UAH',1),
	(213,'乌拉圭','UY','Uruguay','+598','UYU',1),
	(214,'乌兹别克斯坦','UZ','Uzbekistan','+998','UZS',1),
	(215,'西班牙','ES','Spain','+34','EUR',1),
	(216,'西撒哈拉','EH','Western Sahara','+213',NULL,1),
	(217,'希腊','GR','Greece','+30','EUR',1),
	(218,'斐济','FJ','Fiji','+679','FJD',1),
	(219,'新加坡','SG','Singapore','+65','SGD',1),
	(220,'新喀里多尼亚','NC','New Caledonia','+687','CFP',1),
	(221,'新西兰','NZ','New Zealand','+64','NZD',1),
	(222,'匈牙利','HU','Hungary','+336','HUF',1),
	(223,'牙买加','JM','Jamaica','+1809','JMD',1),
	(224,'亚美尼亚','AM','Armenia','+374','AMD',1),
	(225,'也门','YE','Yemen','+967','YER',1),
	(226,'伊拉克','IQ','Iraq','+964','IQD',1),
	(227,'以色列','IL','Israel','+972','ILS',1),
	(228,'意大利','IT','Italy','+39','EUR',1),
	(229,'印度','IN','India','+91','INR',1),
	(230,'印度尼西亚','ID','Indonesia','+62','IDR',1),
	(231,'英国','GB','United Kingdom','+44','GBP',1),
	(232,'英属维尔京群岛','VG','Virgin Islands, British','+1809',NULL,1),
	(233,'英属印度洋领地','IO','British Indian Ocean Territory',NULL,NULL,1),
	(234,'约旦','JO','Jordan','+962','JOD',1),
	(235,'越南','VN','Vietnam','+84','VND',1),
	(236,'赞比亚','ZM','Zambia','+260','ZMW',1),
	(237,'泽西岛','JE','Jersey',NULL,NULL,1),
	(238,'乍得','TD','Chad','+235',NULL,1),
	(239,'直布罗陀','GI','Gibraltar','+350','GIP',1),
	(240,'智利','CL','Chile','+56','CLF',1),
	(241,'中非共和国','CF','Central African Republic','+236','XAF',1),
	(242,'中国','CN','China','+86','CNY',1),
	(243,'中国香港','HK','Hong Kong','+852','HKD',1),
	(244,'中国台湾','TW','Tai Wan','+886','TWD',1),
	(245,'中国澳门','MO','Macao','+853','MOP',1),
	(246,'USDT','USDT','USDT','+1','USDT',2);

/*!40000 ALTER TABLE `sys_countries` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 sys_deposit
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_deposit`;

CREATE TABLE `sys_deposit` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `coin_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '货币代码',
  `bank_id` int NOT NULL COMMENT '银行账户Id',
  `deposit_status` int DEFAULT NULL COMMENT '1 正常 0 失效',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 sys_exchange
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_exchange`;

CREATE TABLE `sys_exchange` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `from_country` varchar(16) DEFAULT NULL,
  `ex_from` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '来源货币代码',
  `target_country` varchar(16) DEFAULT NULL,
  `ex_target` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '兑换货币代码',
  `ex_rate` float(12,4) DEFAULT NULL COMMENT '兑换值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `sys_exchange` WRITE;
/*!40000 ALTER TABLE `sys_exchange` DISABLE KEYS */;

INSERT INTO `sys_exchange` (`id`, `from_country`, `ex_from`, `target_country`, `ex_target`, `ex_rate`, `create_time`, `modified_time`)
VALUES
	(18,'USDT','USDT','CN','CNY',7.0800,'2023-12-01 20:04:03','2023-12-01 20:04:26');

/*!40000 ALTER TABLE `sys_exchange` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 sys_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_code` varchar(20) DEFAULT NULL COMMENT '角色标识',
  `role_name` varchar(20) DEFAULT NULL COMMENT '角色名称',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;

INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `create_time`)
VALUES
	(1,'admin','超级管理员','2021-03-12 13:51:11'),
	(2,'user','普通用户','2021-03-12 13:54:15');

/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 sys_role_auth
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_role_auth`;

CREATE TABLE `sys_role_auth` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int DEFAULT NULL,
  `auth_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色权限关联表';

LOCK TABLES `sys_role_auth` WRITE;
/*!40000 ALTER TABLE `sys_role_auth` DISABLE KEYS */;

INSERT INTO `sys_role_auth` (`id`, `role_id`, `auth_id`)
VALUES
	(1,1,1),
	(2,1,2),
	(3,1,3),
	(4,1,4),
	(5,1,5),
	(6,1,6),
	(7,1,7),
	(8,1,8),
	(9,1,9),
	(10,2,1),
	(11,2,2),
	(12,2,5),
	(13,2,7);

/*!40000 ALTER TABLE `sys_role_auth` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 sys_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '密码',
  `nick_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '昵称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '邮箱',
  `countries` varchar(16) DEFAULT NULL COMMENT '国家代码',
  `area_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '区号',
  `provider_id` varchar(16) DEFAULT NULL COMMENT '租户Id',
  `modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_status` int NOT NULL DEFAULT '0' COMMENT '1正常 0未激活',
  `invite_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '推广码',
  `pay_pass` varchar(128) DEFAULT NULL COMMENT '支付密码，设置时需要通过验证码校验',
  `check_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `parent_id` int DEFAULT '-1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `provider_id` (`provider_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;

INSERT INTO `sys_user` (`id`, `username`, `password`, `nick_name`, `create_time`, `email`, `countries`, `area_code`, `phone`, `provider_id`, `modified_time`, `user_status`, `invite_code`, `pay_pass`, `check_code`, `parent_id`)
VALUES
	(1,'admin','0540aeb4af67b14051dd6bdd420476c9','管理员','2021-03-12 13:50:51','admin@rpay.com',NULL,NULL,NULL,'SUPER-PAY','2023-11-20 11:06:39',1,'SUPER-PAY',NULL,'',-1);

/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 sys_user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联表';

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;

INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`)
VALUES
	(1,1,1);

/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 trade_order
# ------------------------------------------------------------

DROP TABLE IF EXISTS `trade_order`;

CREATE TABLE `trade_order` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `provider_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `name` varchar(128) NOT NULL DEFAULT '',
  `side` int NOT NULL COMMENT '交易类型',
  `market` int NOT NULL COMMENT '交易标的',
  `quantity` decimal(15,5) NOT NULL COMMENT '交易库存',
  `price` decimal(12,5) NOT NULL COMMENT '实际单价',
  `trade_value` decimal(15,5) NOT NULL,
  `esp_price` decimal(12,5) NOT NULL,
  `profit` decimal(12,5) NOT NULL,
  `my_profit` decimal(12,5) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 withdraw_request
# ------------------------------------------------------------

DROP TABLE IF EXISTS `withdraw_request`;

CREATE TABLE `withdraw_request` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `coin_code` varchar(16) NOT NULL COMMENT '出金币种代码',
  `target_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '出金目标币种代码',
  `req_value` double(16,4) NOT NULL COMMENT '出金申请金额',
  `change_value` double(16,4) DEFAULT NULL COMMENT '转汇后金额',
  `withdraw_value` double(16,4) DEFAULT NULL COMMENT '出金实际金额，财务审核时填写',
  `commission` float(12,4) DEFAULT NULL COMMENT '手续费，财务审核是填写',
  `bank_id` int NOT NULL COMMENT '出金收款银行账户，必须是登记后的账户',
  `account_name` varchar(64) DEFAULT NULL,
  `user_id` int NOT NULL COMMENT '申请人',
  `recorded` int DEFAULT NULL COMMENT '审核人',
  `req_status` int NOT NULL COMMENT '申请状态 1 已提交 2 业务审批 3 财务已出金 4 确认到账(超时1天自动确认)',
  `req_proof` int DEFAULT NULL COMMENT '出金合同、物流凭证非必填',
  `withdraw_proof` varchar(128) DEFAULT NULL COMMENT '出金水单截图上传',
  `memo` varchar(128) DEFAULT NULL COMMENT '备注，审核人填，用户无需填写',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

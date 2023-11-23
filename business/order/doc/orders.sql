/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.26 : Database - amusing_order
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE = ''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;
CREATE
    DATABASE /*!32312 IF NOT EXISTS */`amusing_order` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION = 'N' */;

USE
    `amusing_order`;

/*Table structure for table `order_info` */

DROP TABLE IF EXISTS `order_info`;

CREATE TABLE `order_info`
(
    `order_no`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '订单编号',
    `reserve_id`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT '' COMMENT '预定人ID',
    `consignee_id`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT '' COMMENT '收件人ID',
    `total_amount`    int                                                          NOT NULL DEFAULT '0' COMMENT '订单总金额',
    `real_amount`     int                                                          NOT NULL DEFAULT '0' COMMENT '订单实际金额',
    `use_coupon`      tinyint                                                      NOT NULL DEFAULT '2' COMMENT '优惠券：1-使用 2-未使用',
    `coupon_amount`   int                                                          NOT NULL DEFAULT '0' COMMENT '优惠券减免总金额',
    `use_activity`    tinyint                                                      NOT NULL DEFAULT '2' COMMENT '活动：1-参加 2-未参加',
    `activity_amount` int                                                          NOT NULL DEFAULT '0' COMMENT '活动减免总金额',
    `status`          tinyint                                                      NOT NULL DEFAULT '10' COMMENT '订单状态：10-预定中 20-已支付 30-部分退款-部分 35-部分退款-全额 40-全额退款 90-超时关闭 100-已取消',
    `is_freight`      tinyint(1)                                                   NOT NULL DEFAULT '1' COMMENT '包邮：1-包邮 2-不包邮',
    `freight_amount`  int                                                          NOT NULL DEFAULT '0' COMMENT '运费',
    `is_evaluate`     tinyint(1)                                                   NOT NULL DEFAULT '2' COMMENT '评价：1-已评价 2-未评价',
    `pay_id`          bigint                                                                DEFAULT '0' COMMENT '支付ID',
    `can_refund`      tinyint                                                      NOT NULL DEFAULT '1' COMMENT '退款：1-可退 2-不可退',
    `refund_amount`   int                                                          NOT NULL DEFAULT '1' COMMENT '已退款金额',
    `create_by`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`     bigint                                                       NOT NULL COMMENT '创建时间',
    `update_by`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`     bigint                                                       NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`order_no`) USING BTREE,
    KEY `id_no_index` (`reserve_id`, `order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='订单信息';

/*Table structure for table `order_product_info` */

DROP TABLE IF EXISTS `order_product_info`;

CREATE TABLE `order_product_info`
(
    `id`           bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '订单编号',
    `shop_id`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商铺ID',
    `product_id`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品ID',
    `product_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
    `price_id`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '单价ID',
    `price`        decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '单价',
    `num`          int                                                          NOT NULL DEFAULT '0' COMMENT '商品数量',
    `amount`       decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '总金额',
    PRIMARY KEY (`id`),
    KEY `idx_orders` (`order_no`, `shop_id`, `product_id`, `price_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 95
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='订单商铺信息';

/*Table structure for table `order_shops_info` */

DROP TABLE IF EXISTS `order_shops_info`;

CREATE TABLE `order_shops_info`
(
    `id`         bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_no`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
    `shops_id`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商铺ID',
    `shops_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商铺名称',
    `sort`       int(11) unsigned zerofill                                    NOT NULL DEFAULT '00000000000' COMMENT '顺序',
    PRIMARY KEY (`id`),
    KEY `idx_orders` (`order_no`, `shops_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 81
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='订单商铺信息';

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

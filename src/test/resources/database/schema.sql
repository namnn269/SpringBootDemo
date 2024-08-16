DROP TABLE IF EXISTS `m_material`;
CREATE TABLE  `m_material`
(
    `record_id`                 int unsigned NOT NULL AUTO_INCREMENT COMMENT 'レコードID',
    `system_user_code`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'システムユーザーコード',
    `warehouse_group_code`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '拠点グループコード',
    `material_code`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '資材コード',
    `supplier_code`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          DEFAULT '' COMMENT '在庫サプライヤコード',
    `material_name`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         DEFAULT '' COMMENT '資材名',
    `material_short`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          DEFAULT '' COMMENT '資材略称',
    `material_category`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '資材種別',
    `material_procurement_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '資材発注区分',
    `material_size_d`           int                                                   NOT NULL DEFAULT '0' COMMENT '資材サイズD(mm)',
    `material_size_w`           int                                                   NOT NULL DEFAULT '0' COMMENT '資材サイズW(mm)',
    `material_size_h`           int                                                   NOT NULL DEFAULT '0' COMMENT '資材サイズH(mm)',
    `remarks`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         DEFAULT '' COMMENT '備考',
    `create_user_record_id`     int unsigned NOT NULL DEFAULT '0' COMMENT '新規作成ユーザーレコードID',
    `create_date_time`          datetime                                              NOT NULL COMMENT '新規作成日時',
    `update_user_record_id`     int unsigned NOT NULL DEFAULT '0' COMMENT '最終更新ユーザーレコードID',
    `update_date_time`          datetime                                              NOT NULL COMMENT '更新日時',
    `f_delete`                  int                                                            DEFAULT NULL,
    PRIMARY KEY (`record_id`),
    UNIQUE KEY `m_material_uk_1` (`system_user_code`,`warehouse_group_code`,`material_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1796 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='資材マスタ';
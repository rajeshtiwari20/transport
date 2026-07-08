-- Run this against your MySQL database to recreate the tanker-wise report view.
-- Drop first if the view already exists with the old definition.

DROP VIEW IF EXISTS `v_tankerwiser_report`;

CREATE VIEW `v_tankerwiser_report` AS
SELECT
    `u`.`id` AS `id`,
    `u`.`truck_number` AS `truck_number`,
    `u`.`unloading_date` AS `unloading_date`,
    `ld`.`driver1_name` AS `driver_name`,
    `l`.`lr_number` AS `lr_number`,
    `ld`.`from_name` AS `from_name`,
    `ld`.`to_name` AS `to_name`,
    `l`.`consignee_name` AS `consignee_name`,
    `l`.`consignor_name` AS `consignor_name`,
    `lm`.`material_name` AS `material_name`,
    `u`.`weight` AS `weight`,
    `u`.`unloaded_weight` AS `unloaded_weight`,
    `u`.`change_in_weight` AS `difference`,
    `l`.`freight_rate` AS `freight_rate`,
    `l`.`freight_amount` AS `freight_amount`
FROM `loadings` `l`
INNER JOIN `unloadings` `u` ON `l`.`id` = `u`.`loading_id`
LEFT JOIN `loading_details` `ld` ON `l`.`id` = `ld`.`loading_id`
LEFT JOIN `loading_materials` `lm` ON `l`.`id` = `lm`.`loading_id`
WHERE `l`.`status` = 'UNLOADED';

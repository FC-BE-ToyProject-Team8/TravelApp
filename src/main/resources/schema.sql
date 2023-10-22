-- Drop tables if they exist
DROP TABLE IF EXISTS `Itinerary`;
DROP TABLE IF EXISTS `Stay`;
DROP TABLE IF EXISTS `Lodge`;
DROP TABLE IF EXISTS `Move`;
DROP TABLE IF EXISTS `Trip`;

-- Create Trip table
CREATE TABLE `Trip` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date` DATE NULL,
    `is_foreign` TINYINT(1) NOT NULL,
    PRIMARY KEY (`id`)
);

-- Create Move table
CREATE TABLE `Move` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `transportation` VARCHAR(100) NOT NULL,
    `departured_place_name` VARCHAR(100) NOT NULL,
    `arrived_place_name` VARCHAR(100) NULL,
    PRIMARY KEY (`id`)
);

-- Create Lodge table
CREATE TABLE `Lodge` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `lodging_name` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`)
);

-- Create Stay table
CREATE TABLE `Stay` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `place_name` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`)
);

-- Create Itinerary table
CREATE TABLE `Itinerary` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `trip_id` BIGINT NOT NULL,
    `start_date_time` DATETIME NOT NULL,
    `end_date_time` DATETIME NULL,
    `move_id` BIGINT NULL,
    `lodge_id` BIGINT NULL,
    `stay_id` BIGINT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`trip_id`) REFERENCES `Trip`(`id`)
);

-- Drop tables if they exist
DROP TABLE IF EXISTS `Itinerary`;
DROP TABLE IF EXISTS `Trip`;

-- Create Trip table
CREATE TABLE `Trip` (
    `trip_id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date` DATE NULL,
    `is_foreign` TINYINT(1) NOT NULL,
    PRIMARY KEY (`trip_id`)
);

-- Create Itinerary table
CREATE TABLE `Itinerary` (
    `itinerary_id` BIGINT NOT NULL AUTO_INCREMENT,
    `trip_id` BIGINT NOT NULL,
    `start_date_time` DATETIME NOT NULL,
    `end_date_time` DATETIME NULL,
    `move_place_name` VARCHAR(100) NULL,
    `move_address` VARCHAR(255) NULL,
    `lodge_place_name` VARCHAR(100) NULL,
    `lodge_address` VARCHAR(255) NULL,
    `stay_place_name` VARCHAR(100) NULL,
    `stay_address` VARCHAR(255) NULL,
    PRIMARY KEY (`itinerary_id`),
    FOREIGN KEY (`trip_id`) REFERENCES `Trip`(`trip_id`)
);

-- Drop tables if they exist
DROP TABLE IF EXISTS `Itinerary`;
DROP TABLE IF EXISTS `Stay`;
DROP TABLE IF EXISTS `Lodge`;
DROP TABLE IF EXISTS `Move`;
DROP TABLE IF EXISTS `Trip`;
DROP TABLE IF EXISTS `Place`;

-- Create Place table
CREATE TABLE `Place` (
    `place_id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `address` VARCHAR(255) NULL,
    PRIMARY KEY (`place_id`)
);

-- Create Trip table
CREATE TABLE `Trip` (
    `trip_id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date` DATE NULL,
    `is_foreign` TINYINT(1) NOT NULL,
    PRIMARY KEY (`trip_id`)
);

-- Create Move table
CREATE TABLE `Move` (
    `move_id` BIGINT NOT NULL AUTO_INCREMENT,
    `transportation` VARCHAR(100) NOT NULL,
    `departured_place_id` BIGINT NOT NULL,
    `arrived_place_id` BIGINT NULL,
    PRIMARY KEY (`move_id`),
    FOREIGN KEY (`departured_place_id`) REFERENCES `Place`(`place_id`),
    FOREIGN KEY (`arrived_place_id`) REFERENCES `Place`(`place_id`)
);

-- Create Lodge table
CREATE TABLE `Lodge` (
    `lodge_id` BIGINT NOT NULL AUTO_INCREMENT,
    `place_id` BIGINT NOT NULL,
    PRIMARY KEY (`lodge_id`),
    FOREIGN KEY (`place_id`) REFERENCES `Place`(`place_id`)
);

-- Create Stay table
CREATE TABLE `Stay` (
    `stay_id` BIGINT NOT NULL AUTO_INCREMENT,
    `place_id` BIGINT NOT NULL,
    PRIMARY KEY (`stay_id`),
    FOREIGN KEY (`place_id`) REFERENCES `Place`(`place_id`)
);

-- Create Itinerary table
CREATE TABLE `Itinerary` (
    `itinerary_id` BIGINT NOT NULL AUTO_INCREMENT,
    `trip_id` BIGINT NOT NULL,
    `start_date_time` DATETIME NOT NULL,
    `end_date_time` DATETIME NULL,
    `move_id` BIGINT NULL,
    `lodge_id` BIGINT NULL,
    `stay_id` BIGINT NULL,
    PRIMARY KEY (`itinerary_id`),
    FOREIGN KEY (`trip_id`) REFERENCES `Trip`(`trip_id`),
    FOREIGN KEY (`move_id`) REFERENCES `Move`(`move_id`),
    FOREIGN KEY (`lodge_id`) REFERENCES `Lodge`(`lodge_id`),
    FOREIGN KEY (`stay_id`) REFERENCES `Stay`(`stay_id`)
);

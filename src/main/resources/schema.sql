-- Drop tables if they exist
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `like_trip`;
DROP TABLE IF EXISTS `itinerary`;
DROP TABLE IF EXISTS `trip`;
DROP TABLE IF EXISTS `member`;


-- Create Member table
CREATE TABLE `member`
(
    `id`           BIGINT             NOT NULL AUTO_INCREMENT,
    `email`        VARCHAR(500)       NOT NULL,
    `name`         VARCHAR(30)        NOT NULL,
    `nickname`     VARCHAR(30) UNIQUE NOT NULL,
    `password`     VARCHAR(1000)      NOT NULL,
    `created_date` TIMESTAMP          NOT NULL,
    `updated_date` TIMESTAMP          NOT NULL,
    PRIMARY KEY (`id`)
);

-- Create Trip table
CREATE TABLE `trip`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id`    BIGINT       NOT NULL,
    `name`         VARCHAR(100) NOT NULL,
    `start_date`   DATE         NOT NULL,
    `end_date`     DATE         NOT NULL,
    `is_foreign`   boolean      NOT NULL,
    `like_count`   BIGINT       NOT NULL DEFAULT 0,
    `created_date` TIMESTAMP    NOT NULL,
    `updated_date` TIMESTAMP    NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
);

-- Create Itinerary table
CREATE TABLE `itinerary`
(
    `id`                     BIGINT       NOT NULL AUTO_INCREMENT,
    `trip_id`                BIGINT       NOT NULL,
    -- Embedded Route columns
    `transportation`         VARCHAR(255) NULL,
    `departure_place_name`   VARCHAR(100) NULL,
    `departure_address`      VARCHAR(255) NULL,
    `destination_place_name` VARCHAR(100) NULL,
    `destination_address`    VARCHAR(255) NULL,
    `departure_at`           DATETIME     NULL,
    `arrive_at`              DATETIME     NULL,
    -- Embedded Lodge columns
    `lodge_place_name`       VARCHAR(100) NULL,
    `lodge_address`          VARCHAR(255) NULL,
    `check_in_at`            DATETIME     NULL,
    `check_out_at`           DATETIME     NULL,
    -- Embedded Stay columns
    `stay_place_name`        VARCHAR(100) NULL,
    `stay_address`           VARCHAR(255) NULL,
    `start_at`               DATETIME     NULL,
    `end_at`                 DATETIME     NULL,
    `created_date`           TIMESTAMP    NOT NULL,
    `updated_date`           TIMESTAMP    NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`trip_id`) REFERENCES `trip` (`id`) ON DELETE CASCADE
);

-- Create ThumbsUp table
CREATE TABLE `like_trip`
(
    `id`           BIGINT    NOT NULL AUTO_INCREMENT,
    `member_id`    BIGINT    NOT NULL,
    `trip_id`      BIGINT    NOT NULL,
    `created_date` TIMESTAMP NOT NULL,
    `updated_date` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`trip_id`) REFERENCES `trip` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
);

-- Create Comment table
CREATE TABLE `comment`
(
    `id`           BIGINT        NOT NULL AUTO_INCREMENT,
    `member_id`    BIGINT        NOT NULL,
    `trip_id`      BIGINT        NOT NULL,
    `content`      VARCHAR(1000) NOT NULL,
    `created_date` TIMESTAMP     NOT NULL,
    `updated_date` TIMESTAMP     NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`trip_id`) REFERENCES `trip` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS fin_data (
    sensor_id             VARCHAR(100) NOT NULL,
    region_id             INT          NOT NULL,
    visitor_id            VARCHAR(100) NOT NULL,
    device_id             VARCHAR(100),
    device_type           VARCHAR(50),
    population_type       INT,
    global_id             INT,
    first_time_seen       BIGINT       NOT NULL,
    last_time_seen        BIGINT,
    rssi                  INT,
    n_events              INT,
    manufacturer          VARCHAR(100),
    manufacturer_router   TINYINT,
    manufacturer_global   TINYINT,
    excluded              TINYINT,
    ssid                  VARCHAR(255),
    frame_control_hex     VARCHAR(50),
    length                INT
--     , CONSTRAINT pk_fin_data
--         PRIMARY KEY (
--             SENSOR_ID,
--             REGION_ID,
--             VISITOR_ID,
--             FIRST_TIME_SEEN
--         )
);

CREATE TABLE IF NOT EXISTS analytics (
    sensor_id VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    visitors BIGINT NOT NULL,

    PRIMARY KEY (sensor_id, timestamp)
);
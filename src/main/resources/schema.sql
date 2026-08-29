CREATE TABLE IF NOT EXISTS fin_data (
    SENSOR_ID             VARCHAR(100)  NOT NULL,
    REGION_ID             INT           NOT NULL,
    VISITOR_ID            VARCHAR(100)  NOT NULL,
    DEVICE_ID             VARCHAR(100),
    DEVICE_TYPE           VARCHAR(50),
    POPULATION_TYPE       INT,
    GLOBAL_ID             INT,
    FIRST_TIME_SEEN       BIGINT        NOT NULL,
    LAST_TIME_SEEN        BIGINT,
    RSSI                   INT,
    N_EVENTS               INT,
    MANUFACTURER           VARCHAR(100),
    MANUFACTURER_ROUTER    TINYINT,
    MANUFACTURER_GLOBAL    TINYINT,
    EXCLUDED               TINYINT,
    SSID                   VARCHAR(255),
    FRAME_CONTROL_HEX      VARCHAR(50),
    LENGTH                 INT,

    CONSTRAINT pk_fin_data
        PRIMARY KEY (
            SENSOR_ID,
            REGION_ID,
            VISITOR_ID,
            FIRST_TIME_SEEN
        )
);

CREATE TABLE IF NOT EXISTS analytics (
    sensor_id VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    visitors BIGINT NOT NULL,

    PRIMARY KEY (sensor_id, timestamp)
);
CREATE TABLE FIN_DATA (
    SENSOR_ID             VARCHAR2(100)  NOT NULL,
    REGION_ID             NUMBER(10)     NOT NULL,
    VISITOR_ID            VARCHAR2(100)  NOT NULL,
    DEVICE_ID             VARCHAR2(100),
    DEVICE_TYPE           VARCHAR2(50),
    POPULATION_TYPE       NUMBER(10),
    GLOBAL_ID             NUMBER(10),
    FIRST_TIME_SEEN       NUMBER(19)     NOT NULL,
    LAST_TIME_SEEN        NUMBER(19),
    RSSI                   NUMBER(10),
    N_EVENTS               NUMBER(10),
    MANUFACTURER           VARCHAR2(100),
    MANUFACTURER_ROUTER    NUMBER(1),
    MANUFACTURER_GLOBAL    NUMBER(1),
    EXCLUDED               NUMBER(1),
    SSID                   VARCHAR2(255),
    FRAME_CONTROL_HEX      VARCHAR2(50),
    LENGTH                 NUMBER(10),

    CONSTRAINT PK_FIN_DATA
        PRIMARY KEY (
            SENSOR_ID,
            REGION_ID,
            VISITOR_ID,
            FIRST_TIME_SEEN
        )
);


CREATE TABLE analytics (
    sensor_id VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    visitors BIGINT NOT NULL,

    PRIMARY KEY (sensor_id, timestamp)
);
package com.msk.batch.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Track {

    String sensorId;
    int regionId;
    String visitorId;
    String deviceId;
    String deviceType;
    long firstTimeSeen;
    long lastTimeSeen;

    public Track(String sensorId, int regionId, String visitorId, String deviceId, String deviceType, long firstTimeSeen, long lastTimeSeen) {
        this.sensorId = sensorId;
        this.regionId = regionId;
        this.visitorId = visitorId;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.firstTimeSeen = firstTimeSeen;
        this.lastTimeSeen = lastTimeSeen;
    }
}

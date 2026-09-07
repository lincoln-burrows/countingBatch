package com.msk.batch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@IdClass(FinDataId.class)
@Getter
public class FinData {
//todo: private 필드로 변경 -> 데이터 은닉, 접근권한을 최소화
    @Id
    String sensorId;

    @Id
    int regionId;

    @Id
    String visitorId;

    String deviceId;
    String deviceType;
    int populationType;
    int globalId;

    @Id
    long firstTimeSeen;
    long lastTimeSeen;
    int rssi;
    int nEvents;
    String manufacturer;
    boolean manufacturerRouter;
    boolean manufacturerGlobal;
    boolean excluded;
    String ssid;
    String frameControlHex;
    int length;

    public FinData(String sensorId, int regionId, String visitorId, String deviceId, String deviceType, int populationType, int globalId, long firstTimeSeen, long lastTimeSeen, int rssi, int nEvents, String manufacturer, boolean manufacturerRouter, boolean manufacturerGlobal, boolean excluded, String ssid, String frameControlHex, int length) {
        this.sensorId = sensorId;
        this.regionId = regionId;
        this.visitorId = visitorId;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.populationType = populationType;
        this.globalId = globalId;
        this.firstTimeSeen = firstTimeSeen;
        this.lastTimeSeen = lastTimeSeen;
        this.rssi = rssi;
        this.nEvents = nEvents;
        this.manufacturer = manufacturer;
        this.manufacturerRouter = manufacturerRouter;
        this.manufacturerGlobal = manufacturerGlobal;
        this.excluded = excluded;
        this.ssid = ssid;
        this.frameControlHex = frameControlHex;
        this.length = length;
    }
}

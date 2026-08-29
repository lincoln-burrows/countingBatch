package com.msk.batch.model;


public record FinData (
    String sensorId,
    int regionId,
    String visitorId,
    String deviceId,
    String deviceType,
    int populationType,
    int globalId,
    long firstTimeSeen,
    long lastTimeSeen,
    int rssi,
    int nEvents,
    String manufacturer,
    boolean manufacturerRouter,
    boolean manufacturerGlobal,
    boolean excluded,
    String ssid,
    String frameControlHex,
    int length
){
}


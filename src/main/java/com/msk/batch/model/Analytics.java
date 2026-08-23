package com.msk.batch.model;
import java.time.LocalDateTime;


public record Analytics (

    String sensorId,
    LocalDateTime timestamp,
    long visitors
){

}

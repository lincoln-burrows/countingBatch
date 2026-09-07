package com.msk.batch.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FinDataId implements Serializable {

    private String sensorId;
    private int regionId;
    private String visitorId;
    private long firstTimeSeen;

}

package com.msk.batch.service;

import com.msk.batch.model.Analytics;
import com.msk.batch.model.FinData;
import com.msk.batch.model.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AggregateService {

    private final JdbcTemplate jdbcTemplate;

    public void aggregate() {
        List<FinData> finDataList = loadFinData();

        List<Track> tracks = makeTracks(finDataList);

        saveAnalytics(tracks);

    }

    private void saveAnalytics(List<Track> tracks) {
        if(tracks.size() > 0){
            Analytics analytics = new Analytics(tracks.get(0).getSensorId(), LocalDateTime.now(), tracks.size());

            String sql = """
                INSERT INTO analytics (
                    sensor_id,
                    timestamp,
                    visitors
                )
                VALUES (?, ?, ?)
                """;

            jdbcTemplate.update(
                    sql,
                    analytics.sensorId(),
                    analytics.timestamp(),
                    analytics.visitors()
            );
        }
    }

    private List<Track> makeTracks(List<FinData> finDataList) {

        HashMap<String, Track> visitors = new HashMap<>();

        for (FinData newData :  finDataList) {

            // 멀리 있는 이상신호 제거
            if(newData.rssi() < -80 ) continue;

            if (visitors.containsKey(newData.visitorId())){
                Track data = visitors.get(newData.visitorId());
                data.setFirstTimeSeen(Math.min(data.getFirstTimeSeen(), newData.firstTimeSeen()));
                data.setFirstTimeSeen(Math.max(data.getLastTimeSeen(), newData.lastTimeSeen()));

            } else {
                visitors.put(newData.visitorId(),
                        new Track(newData.sensorId(), newData.regionId(), newData.visitorId(), newData.deviceId(), newData.deviceType(), newData.firstTimeSeen(), newData.lastTimeSeen()));
            }
        }

        return visitors.values().stream().toList();
    }


    private List<FinData> loadFinData() {
        return jdbcTemplate.query(
                "SELECT * FROM fin_data order by visitor_id, last_time_seen",
                (rs, rowNum) -> new FinData(
                        rs.getString("sensor_id"),
                        rs.getInt("region_id"),
                        rs.getString("visitor_id"),
                        rs.getString("device_id"),
                        rs.getString("device_type"),
                        rs.getInt("population_type"),
                        rs.getInt("global_id"),
                        rs.getLong("first_time_seen"),
                        rs.getLong("last_time_seen"),
                        rs.getInt("rssi"),
                        rs.getInt("n_events"),
                        rs.getString("manufacturer"),
                        rs.getBoolean("manufacturer_router"),
                        rs.getBoolean("manufacturer_global"),
                        rs.getBoolean("excluded"),
                        rs.getString("ssid"),
                        rs.getString("frame_control_hex"),
                        rs.getInt("length")
                )
        );
    }

}

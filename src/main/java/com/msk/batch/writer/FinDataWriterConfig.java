package com.msk.batch.writer;

import com.msk.batch.model.FinData;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FinDataWriterConfig {

    @Bean
    public JdbcBatchItemWriter<FinData> finDataWriter(DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<FinData>()
                .dataSource(dataSource)
                .sql("""
                    INSERT INTO FIN_DATA (
                        SENSOR_ID,
                        REGION_ID,
                        VISITOR_ID,
                        DEVICE_ID,
                        DEVICE_TYPE,
                        POPULATION_TYPE,
                        GLOBAL_ID,
                        FIRST_TIME_SEEN,
                        LAST_TIME_SEEN,
                        RSSI,
                        N_EVENTS,
                        MANUFACTURER,
                        MANUFACTURER_ROUTER,
                        MANUFACTURER_GLOBAL,
                        EXCLUDED,
                        SSID,
                        FRAME_CONTROL_HEX,
                        LENGTH
                    )
                    VALUES (
                        :sensorId,
                        :regionId,
                        :visitorId,
                        :deviceId,
                        :deviceType,
                        :populationType,
                        :globalId,
                        :firstTimeSeen,
                        :lastTimeSeen,
                        :rssi,
                        :nEvents,
                        :manufacturer,
                        :manufacturerRouter,
                        :manufacturerGlobal,
                        :excluded,
                        :ssid,
                        :frameControlHex,
                        :length
                    )
                    """)
                .beanMapped()
                .build();
    }
}

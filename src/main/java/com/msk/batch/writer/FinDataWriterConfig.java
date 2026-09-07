package com.msk.batch.writer;

import com.msk.batch.model.FinData;
import com.msk.batch.repository.FinDataRepository;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class FinDataWriterConfig {

    @Bean
    public ItemWriter<FinData> finDataJpaBulkWriter(
            FinDataRepository finDataRepository) {

        return chunk -> {
            finDataRepository.saveAll(chunk.getItems());
        };
    }

    @Bean
    public JpaItemWriter<FinData> finDataJpaWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<FinData>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }


    @Bean
    public JdbcBatchItemWriter<FinData> finDataWriter(DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<FinData>()
                .dataSource(dataSource)
                .sql("""
                    INSERT INTO fin_data (
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

    @Bean
    public ItemWriter<FinData> finDataBulkWriter(JdbcTemplate jdbcTemplate) {

        return chunk -> {

            StringBuilder sql = new StringBuilder("""
                    INSERT INTO fin_data (
                        sensor_id,
                        region_id,
                        visitor_id,
                        device_id,
                        device_type,
                        population_type,
                        global_id,
                        first_time_seen,
                        last_time_seen,
                        rssi,
                        n_events,
                        manufacturer,
                        manufacturer_router,
                        manufacturer_global,
                        excluded,
                        ssid,
                        frame_control_hex,
                        length
                    )
                    VALUES
                    """);

            String placeholder =
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            for (int i = 0; i < chunk.size(); i++) {
                sql.append(placeholder);

                if (i < chunk.size() - 1) {
                    sql.append(", ");
                }
            }

            List<Object> params = new ArrayList<>(chunk.size() * 18);
//todo : jdbctemplate 활용 시 더 깔끔
            // jpa 를 하고 파라미터를 붙인다 , 파라미터 값들이 mysql 을 최적화 하기 위한 옵션, 그 옵션들을 알아보라 커넥션을 맺을 때 옵션을 줘야함
            // 결과 적으로 왜 jdbc 가 나은지 설명할 수 있어야
            // 테스트 케이스.. jdbc / jpa
            // 타인이 보기에도 잘 된 테스트구나 느껴질 정도
            // 어떻게 문서를 잘 쓸 수 있고 상시 고민할 것 .. jdbc 는 mysql 의 특수 기능을 사용할 때.
            for (FinData data : chunk) {
                params.add(data.getSensorId());
                params.add(data.getRegionId());
                params.add(data.getVisitorId());
                params.add(data.getDeviceId());
                params.add(data.getDeviceType());
                params.add(data.getPopulationType());
                params.add(data.getGlobalId());
                params.add(data.getFirstTimeSeen());
                params.add(data.getLastTimeSeen());
                params.add(data.getRssi());
                params.add(data.getNEvents());
                params.add(data.getManufacturer());
                params.add(data.isManufacturerRouter());
                params.add(data.isManufacturerGlobal());
                params.add(data.isExcluded());
                params.add(data.getSsid());
                params.add(data.getFrameControlHex());
                params.add(data.getLength());
            }

            jdbcTemplate.update(
                    sql.toString(),
                    params.toArray()
            );
        };
    }
}

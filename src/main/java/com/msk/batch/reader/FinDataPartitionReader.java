package com.msk.batch.reader;

import com.msk.batch.model.FinData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@Slf4j
public class FinDataPartitionReader {

    @Bean
    @StepScope
    @Qualifier
    public FlatFileItemReader<FinData> partitionFinDataItemReader(
            @Value("#{stepExecutionContext['filePath']}") String filePath) {

        return new FlatFileItemReaderBuilder<FinData>()
                .name("partitionFinDataItemReader")
                .resource(new FileSystemResource(filePath))
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names(
                        "sensorId",
                        "regionId",
                        "visitorId",
                        "deviceId",
                        "deviceType",
                        "populationType",
                        "globalId",
                        "firstTimeSeen",
                        "lastTimeSeen",
                        "rssi",
                        "nEvents",
                        "manufacturer",
                        "manufacturerRouter",
                        "manufacturerGlobal",
                        "excluded",
                        "ssid",
                        "frameControlHex",
                        "length"
                )
                .fieldSetMapper(fieldSet -> new FinData(
                        fieldSet.readString("sensorId"),
                        fieldSet.readInt("regionId"),
                        fieldSet.readString("visitorId"),
                        fieldSet.readString("deviceId"),
                        fieldSet.readString("deviceType"),
                        fieldSet.readInt("populationType"),
                        fieldSet.readInt("globalId"),
                        fieldSet.readLong("firstTimeSeen"),
                        fieldSet.readLong("lastTimeSeen"),
                        fieldSet.readInt("rssi"),
                        fieldSet.readInt("nEvents"),
                        fieldSet.readString("manufacturer"),
                        fieldSet.readBoolean("manufacturerRouter"),
                        fieldSet.readBoolean("manufacturerGlobal"),
                        fieldSet.readBoolean("excluded"),
                        fieldSet.readString("ssid"),
                        fieldSet.readString("frameControlHex"),
                        fieldSet.readInt("length")
                ))
                .build();
    }
}

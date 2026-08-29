package com.msk.batch.reader;

import com.msk.batch.model.FinData;

import jakarta.annotation.Nonnull;
import lombok.NonNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class FinDataReader {

    private static final DateTimeFormatter titleFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    @Bean
    @StepScope
    public MultiResourceItemReader<FinData> multiResourceReader(FlatFileItemReader<FinData> flatFileItemReader) throws Exception{
        // todo: 노란줄 제거, 하드코딩 주소 -> yml 으로
        Path dirPath = Paths.get("data/sensors/sensor1234");

        Resource[] resources = findRecentSensorFiles(dirPath);

        return new MultiResourceItemReaderBuilder<FinData>()
                .name("multiResourceItemReader")
                .resources(resources)
                .delegate(flatFileItemReader)
                .build();
    }

    @Bean
    public FlatFileItemReader<FinData> finDataItemReader() {

        return new FlatFileItemReaderBuilder<FinData>()
                .name("flatfileReader")
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


    private Resource[] findRecentSensorFiles(Path dirPath) throws IOException {

        List<Resource> files = new ArrayList<>();
        //파일명 - 1234_20260821T090110.csv
        try (DirectoryStream<Path> directoryStream =
                     Files.newDirectoryStream(dirPath, "*.csv")) {
            // todo : stream 방식으로 바꿀 것  - 전처리 기능과 try 안에 // try 문 안에 있는걸 별도 메소드로 뺄 것. 가독성 안좋음. for 룹 안에는 4~5줄.

            for (Path path : directoryStream) {

                String fileName = path.getFileName().toString();

                int underscoreIndex = fileName.indexOf("_");
                int dotIndex = fileName.lastIndexOf(".");

                if (underscoreIndex == -1 || dotIndex == -1) {
                    continue;
                }

                String timestamp = fileName.substring(underscoreIndex + 1, dotIndex);

                try {
                    // todo : 메소드로 뺄 것
                    LocalDateTime fileTime = LocalDateTime.parse(timestamp, titleFormatter);

                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime threeMinutesAgo = now.minusMinutes(3);
// 테스트 환경에서 주석처리
                    if (!fileTime.isBefore(threeMinutesAgo)
                            && !fileTime.isAfter(now)) {

                        files.add(new FileSystemResource(path.toFile()));
                    }
//                    files.add(new FileSystemResource(path.toFile()));

                } catch (DateTimeParseException e) {
                    // 파일명 형식이 다른 파일은 무시 -> 로그로 남길 것

                }
            }
        }
        return files.toArray(new Resource[0]);
    }
}

package com.msk.batch.partition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class FilePartitioner implements Partitioner {

    private static final DateTimeFormatter titleFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Path dirPath = Paths.get("data/sensors/sensor1001");

        Resource[] resources = null;
        try {
            resources = findRecentSensorFiles(dirPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, ExecutionContext> partitions = new HashMap<>();

        for (int i = 0; i < resources.length; i++) {

            ExecutionContext context = new ExecutionContext();

            try {
                context.putString(
                        "filePath",
                        resources[i].getFile().getAbsolutePath()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            partitions.put("partition" + i, context);
        }

        return partitions;
    }

    private Resource[] findRecentSensorFiles(Path dirPath) throws IOException {

        List<Resource> files = new ArrayList<>();
        //파일명 - 1234_20260821T090110.csv
        try (DirectoryStream<Path> directoryStream =
                     Files.newDirectoryStream(dirPath, "*.csv")) {

            // todo : stream 방식으로 바꿀 것  - 전처리 기능과 try 안에 // try 문 안에 있는걸 별도 메소드로 뺄 것. 가독성 안좋음. for 룹 안에는 4~5줄.
            directoryStream.forEach(path -> include3minFile(path, files));

        }
        return files.toArray(new org.springframework.core.io.Resource[0]);
    }

    private void include3minFile(Path path, List<Resource> files) {

        String fileName = path.getFileName().toString();
        int underscoreIndex = fileName.indexOf("_");
        int dotIndex = fileName.lastIndexOf(".");

        if(validTitleChk(underscoreIndex, dotIndex) && validTimechk(fileName, underscoreIndex, dotIndex)){
            files.add(new FileSystemResource(path.toFile()));
        }
    }

    private boolean validTimechk(String fileName, int underscoreIndex, int dotIndex) {

        String timestamp = fileName.substring(underscoreIndex + 1, dotIndex);

        try {
            // todo : 메소드로 뺄 것 - 완료
            LocalDateTime fileTime = LocalDateTime.parse(timestamp, titleFormatter);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime threeMinutesAgo = now.minusMinutes(3);
            // 테스트 환경에서 주석처리
//            return !fileTime.isBefore(threeMinutesAgo)
//                    && !fileTime.isAfter(now);
            return true;
        } catch (DateTimeParseException e) {
            // 파일명 형식이 다른 파일은 무시 -> 로그로 남길 것
            log.error(e.getMessage());
            return false;
        }
    }

    private boolean validTitleChk(int underscoreIndex, int dotIndex) {
        return underscoreIndex != -1 && dotIndex != -1;
    }

}

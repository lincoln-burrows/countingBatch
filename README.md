# Sensor Batch

센서 FIN CSV를 `Event → Track → Analytics` 순서로 처리해 다음 결과를 insert 하는 싱글 스레드 Spring Batch 프로젝트입니다.

추후 확장: SiteXXXX ∋ sensor1000(region1), sensor1001(region2), sensor1002(region3)...  
멀티스레드 처리 및 visitor 수 단순 합으로 site의 visitor count 구할 예정

```ANALYTICS
sensorId, timestamp, visitor_count
SENSOR-1234, 2026-08-23 09:16:35.38183, 16383
```

## 처리 규칙

1. `data/sensors/sensor1234/*.csv`를 센서 폴더에서 찾습니다.
2. 실행 시각 직전 3분 구간 csv를 읽습니다. 
3. `(sensorId, visitorId)`가 같은 Event를 하나의 Track으로 병합합니다.
4. 집계된 tracks 의 수를 세어 sensorId, timestamp, visitor_count 형태로 insert 합니다. 


## 실행

Spring Batch 메타데이터는 기본적으로 `./data/sensor-batch-metadata` H2 파일에 영속화되어 동일 Job 파라미터의 중복 실행을 막고 실패 Step 재시작 정보를 유지합니다.

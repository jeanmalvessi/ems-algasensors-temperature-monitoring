package com.algaworks.algasensors.temperature.monitoring.domain.service;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLog;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLogId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemperatureMonitoringService {

    private final SensorMonitoringRepository sensorMonitoringRepository;
    private final TemperatureLogRepository temperatureLogRepository;

    @Transactional
    public void processTemperature(TemperatureLogData data) {
        this.sensorMonitoringRepository.findById(new SensorId(data.getSensorId()))
                .ifPresentOrElse(sensor -> handleSensorMonitoring(data, sensor),
                        () -> logIgnored(data));
    }

    private void handleSensorMonitoring(TemperatureLogData data, SensorMonitoring sensor) {
        if (sensor.isEnabled()) {
            sensor.setLastTemperature(data.getValue());
            sensor.setUpdatedAt(OffsetDateTime.now());
            this.sensorMonitoringRepository.save(sensor);

            TemperatureLog log = TemperatureLog.builder()
                    .id(new TemperatureLogId(data.getId()))
                    .registeredAt(data.getRegisteredAt())
                    .value(data.getValue())
                    .sensorId(new SensorId(data.getSensorId()))
                    .build();
            this.temperatureLogRepository.save(log);
        } else {
            this.logIgnored(data);
        }
    }

    private void logIgnored(TemperatureLogData data) {
        log.info("Temperature ignored: SensorId {} Temperature {}", data.getSensorId(), data.getValue());
    }
}

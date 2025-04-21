package com.algaworks.algasensors.temperature.monitoring.domain.service;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorAlertService {

    private final SensorAlertRepository repository;

    @Transactional
    public void handleAlert(TemperatureLogData data) {
        this.repository.findById(new SensorId(data.getSensorId()))
                .ifPresentOrElse(alert -> {
                    if (alert.getMaxTemperature() != null && data.getValue().compareTo(alert.getMaxTemperature()) >= 0) {
                        log.info("Alert max temperature: SensorId {} Temperature {}", data.getSensorId(), data.getValue());
                    } else if (alert.getMinTemperature() != null && data.getValue().compareTo(alert.getMinTemperature()) <= 0) {
                        log.info("Alert min temperature: SensorId {} Temperature {}", data.getSensorId(), data.getValue());
                    } else {
                        this.logIgnoredData(data);
                    }
                }, () -> this.logIgnoredData(data));
    }

    private void logIgnoredData(TemperatureLogData data) {
        log.info("Alert ignored: SensorId {} Temperature {}", data.getSensorId(), data.getValue());
    }
}

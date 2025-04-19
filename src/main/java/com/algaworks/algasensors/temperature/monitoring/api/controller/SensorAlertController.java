package com.algaworks.algasensors.temperature.monitoring.api.controller;

import com.algaworks.algasensors.temperature.monitoring.api.model.SensorAlertInput;
import com.algaworks.algasensors.temperature.monitoring.api.model.SensorAlertOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorAlert;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
@RequiredArgsConstructor
public class SensorAlertController {

    private final SensorAlertRepository repository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SensorAlertOutput getOne(@PathVariable SensorId sensorId) {
        SensorAlert alert = findById(sensorId);
        return this.convertToModel(alert);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public SensorAlertOutput createOrUpdate(@PathVariable SensorId sensorId, @RequestBody SensorAlertInput input) {
        SensorAlert alert = findByIdOrDefault(sensorId, input);
        alert.setMaxTemperature(input.getMaxTemperature());
        alert.setMinTemperature(input.getMinTemperature());

        this.repository.saveAndFlush(alert);

        return this.convertToModel(alert);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable SensorId sensorId) {
        SensorAlert alert = findById(sensorId);
        this.repository.delete(alert);
    }

    private SensorAlert findById(SensorId sensorId) {
        return this.repository.findById(sensorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private SensorAlert findByIdOrDefault(SensorId sensorId, SensorAlertInput input) {
        return this.repository.findById(sensorId)
                .orElse(SensorAlert.builder()
                        .id(sensorId)
                        .maxTemperature(null)
                        .minTemperature(null)
                        .build());
    }

    private SensorAlertOutput convertToModel(SensorAlert alert) {
        return SensorAlertOutput.builder()
                .id(alert.getId().getValue())
                .maxTemperature(alert.getMaxTemperature())
                .minTemperature(alert.getMinTemperature())
                .build();
    }
}

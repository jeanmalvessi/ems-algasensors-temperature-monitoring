# temperature-monitoring
This is a project built during the AlgaWorks Microservices Specialist Course (EMS) Level 1 - Software architecture and microservices.

See full description on [ems-algasensors-meta](https://github.com/jeanmalvessi/ems-algasensors-meta).

Temperatures data analysis and reports.

Endpoints:
- `GET /api/sensors/{sensorId}/temperatures`: retrieve all temperature registrations
- `PUT /api/sensors/{sensorId}/alert`: update a specific temperature alert configuration
- `GET /api/sensors/{sensorId}/alert`: retrieve a specific temperature alert configuration
- `DELETE /api/sensors/{sensorId}/alert`: delete a specific temperature alert configuration
- `GET /api/sensors/{sensorId}/monitoring`: retrieve a specific temperature monitoring
- `PUT /api/sensors/{sensorId}/monitoring/enable`: activate a specific temperature monitoring
- `DELETE /api/sensors/{sensorId}/monitorig/enable`: deactivate a specific temperature monitoring

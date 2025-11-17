# Logging Configuration

This document describes the logging configuration for the Cloud Vendor API application.

## Log Files Location

All log files are stored in the `logs/` directory at the root of the project.

## Log Files

### 1. Application Logs
- **File**: `logs/cloud-vendor-api.log`
- **Content**: All application logs (INFO, DEBUG, WARN, ERROR)
- **Rotation**: 
  - Daily rotation
  - File size limit: 10MB per file
  - Retention: 30 days
  - Total size cap: 1GB

### 2. Error Logs
- **File**: `logs/cloud-vendor-api-error.log`
- **Content**: Only ERROR level logs
- **Rotation**: 
  - Daily rotation
  - File size limit: 10MB per file
  - Retention: 60 days
  - Total size cap: 500MB

## Log Format

All log entries follow this format:
```
yyyy-MM-dd HH:mm:ss.SSS [thread] LEVEL logger - message
```

Example:
```
2024-01-15 10:30:45.123 [http-nio-9090-exec-1] INFO  c.e.c.controller.CloudVendorController - POST /cloudvendor - Creating new cloud vendor with ID: V001
```

## Log Levels

### Application Loggers
- **com.example.cloudvendor**: DEBUG level
  - Includes all application classes (controllers, services, repositories)
  - Provides detailed debugging information

### Framework Loggers
- **org.springframework**: INFO level
  - Spring Framework logs
- **org.hibernate**: INFO level
  - Hibernate ORM logs
- **org.hibernate.SQL**: DEBUG level
  - SQL query logs

### Root Logger
- **Default**: INFO level
- **Development/Default Profile**: DEBUG level
- **Production Profile**: INFO level

## Log Appenders

### 1. Console Appender
- Outputs logs to the console/terminal
- Used during development for immediate feedback

### 2. File Appender
- Writes all logs to `cloud-vendor-api.log`
- Includes log rotation based on size and time

### 3. Error File Appender
- Writes only ERROR level logs to `cloud-vendor-api-error.log`
- Useful for monitoring critical issues

## Configuration Files

### logback-spring.xml
The main logging configuration file located at `src/main/resources/logback-spring.xml`. This file:
- Defines log file paths and rotation policies
- Configures appenders (console, file, error file)
- Sets log levels for different packages
- Supports Spring profiles (dev, prod)

### application.yml
Contains logging level overrides and references the logback configuration:
```yaml
logging:
  config: classpath:logback-spring.xml
  file:
    name: logs/cloud-vendor-api.log
  level:
    com.example.cloudvendor: DEBUG
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
```

## Logging in Code

All classes use SLF4J logger:
```java
private static final Logger logger = LoggerFactory.getLogger(ClassName.class);
```

### Log Levels Usage
- **DEBUG**: Detailed information for debugging (method entry, flow)
- **INFO**: General informational messages (successful operations)
- **WARN**: Warning messages (business logic warnings, not found)
- **ERROR**: Error messages (exceptions, failures)

## Viewing Logs

### During Development
- Logs are displayed in the console
- Log files are written to `logs/` directory

### In Production
- Check log files in the `logs/` directory
- Monitor `cloud-vendor-api-error.log` for critical issues
- Use log aggregation tools (ELK, Splunk, etc.) for centralized logging

## Log Rotation

Logs are automatically rotated:
- When file size reaches 10MB
- Daily at midnight
- Old files are automatically deleted based on retention policy

## Best Practices

1. **Use appropriate log levels**: Don't log sensitive information
2. **Use parameterized logging**: `logger.info("User {} logged in", userId)` instead of string concatenation
3. **Monitor error logs**: Regularly check `cloud-vendor-api-error.log`
4. **Log important operations**: All CRUD operations are logged
5. **Include context**: Log relevant IDs, parameters, and results

## Troubleshooting

### Logs not appearing
1. Check if `logs/` directory exists and is writable
2. Verify `logback-spring.xml` is in `src/main/resources`
3. Check application startup logs for configuration errors

### Log files too large
- Logs are automatically rotated
- Adjust `maxFileSize` and `maxHistory` in `logback-spring.xml` if needed

### Missing logs
- Verify log level settings in `application.yml`
- Check if the logger is correctly initialized in the class


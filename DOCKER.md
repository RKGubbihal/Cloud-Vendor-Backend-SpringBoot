# Docker Setup Guide

This guide explains how to build and run the Cloud Vendor API application using Docker.

## Prerequisites

- Docker Desktop (or Docker Engine) installed
- Docker Compose installed (usually comes with Docker Desktop)

## Quick Start

### Option 1: Using Docker Compose (Recommended)

This will start both the application and MySQL database:

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop all services
docker-compose down

# Stop and remove volumes (clean slate)
docker-compose down -v
```

### Option 2: Using Dockerfile Only

If you have MySQL running separately:

```bash
# Build the Docker image
docker build -t cloud-vendor-api:latest .

# Run the container
docker run -d \
  --name cloud-vendor-api \
  -p 9090:9090 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/vendor_mgmt_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=MySQLhandson#2023 \
  -v $(pwd)/logs:/app/logs \
  cloud-vendor-api:latest

# View logs
docker logs -f cloud-vendor-api

# Stop the container
docker stop cloud-vendor-api

# Remove the container
docker rm cloud-vendor-api
```

## Dockerfile Details

The Dockerfile uses a multi-stage build:

1. **Build Stage**: Uses Maven to compile and package the application
2. **Runtime Stage**: Uses a lightweight JRE image to run the application

### Key Features:
- Multi-stage build for smaller final image
- Non-root user for security
- Health check endpoint configured
- Logs directory mounted as volume
- Port 9090 exposed

## Docker Compose Services

### MySQL Service
- **Image**: mysql:8.0
- **Port**: 3306 (mapped to host)
- **Database**: vendor_mgmt_db
- **Root Password**: MySQLhandson#2023
- **Data Persistence**: Volume `mysql_data`

### Application Service
- **Port**: 9090 (mapped to host)
- **Depends on**: MySQL service
- **Health Check**: `/cloudvendor/health` endpoint
- **Logs**: Mounted to `./logs` directory

## Environment Variables

You can override environment variables in `docker-compose.yml` or pass them via `-e` flag:

- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Hibernate DDL mode (update, create, etc.)

## Accessing the Application

Once the containers are running:

- **API Base URL**: http://localhost:9090/cloudvendor
- **Health Check**: http://localhost:9090/cloudvendor/health
- **Swagger UI**: http://localhost:9090/swagger-ui.html
- **API Docs**: http://localhost:9090/api-docs

## Viewing Logs

### Application Logs
```bash
# Using docker-compose
docker-compose logs -f app

# Using docker
docker logs -f cloud-vendor-api

# View log files (if volume mounted)
tail -f logs/cloud-vendor-api.log
```

### MySQL Logs
```bash
docker-compose logs -f mysql
```

## Database Access

### From Host Machine
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p
# Password: MySQLhandson#2023
```

### From Another Container
```bash
docker exec -it cloud-vendor-mysql mysql -u root -p
```

## Troubleshooting

### Container won't start
1. Check if port 9090 is already in use:
   ```bash
   netstat -an | grep 9090
   # or
   lsof -i :9090
   ```

2. Check container logs:
   ```bash
   docker-compose logs app
   ```

### Database connection issues
1. Ensure MySQL container is healthy:
   ```bash
   docker-compose ps
   ```

2. Check MySQL logs:
   ```bash
   docker-compose logs mysql
   ```

3. Verify database credentials in `docker-compose.yml`

### Application can't connect to database
1. Ensure the database URL uses the service name `mysql` (not `localhost`)
2. Wait for MySQL to be fully ready (health check should pass)
3. Check network connectivity:
   ```bash
   docker exec cloud-vendor-api ping mysql
   ```

## Building for Production

### Build with specific tag
```bash
docker build -t cloud-vendor-api:v1.0.0 .
```

### Push to Docker Registry
```bash
# Tag for registry
docker tag cloud-vendor-api:v1.0.0 your-registry/cloud-vendor-api:v1.0.0

# Push
docker push your-registry/cloud-vendor-api:v1.0.0
```

## Production Considerations

1. **Use Environment Variables**: Don't hardcode passwords in docker-compose.yml
2. **Use Secrets Management**: Use Docker secrets or external secret management
3. **Resource Limits**: Add resource constraints in docker-compose.yml
4. **Network Security**: Use proper network isolation
5. **Log Aggregation**: Set up centralized logging
6. **Monitoring**: Add monitoring and alerting
7. **Backup Strategy**: Regular database backups

## Clean Up

```bash
# Stop and remove containers
docker-compose down

# Remove volumes (deletes database data)
docker-compose down -v

# Remove images
docker rmi cloud-vendor-api:latest

# Remove all unused Docker resources
docker system prune -a
```

## Health Checks

The application includes health checks:
- **Application**: Checks `/cloudvendor/health` endpoint
- **MySQL**: Uses `mysqladmin ping`

View health status:
```bash
docker-compose ps
```

## Volumes

- `mysql_data`: Persistent storage for MySQL data
- `./logs`: Application logs directory (mounted from host)

## Network

All services run on the `cloud-vendor-network` bridge network, allowing them to communicate using service names.


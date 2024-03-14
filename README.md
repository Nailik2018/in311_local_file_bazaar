# Readme in311_local_file_bazaar
## Installation
### Install docker Container
``` bash
docker-compose up -d
```
### environemnt Datei erstellen
## Projekt starten
### Start dev
```bash
./mvnw quarkus:dev 
```

## Verwendete Ports
| Port | Verwendung |
|------|------------|
| 3306 | MySQL      |
| 8080 | PhpMyAdmin |
| 8882 | Quarkus    |


## URLs
### Swagger UI
http://localhost:8882/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/swagger-ui
### OpenAPI
http://localhost:8882/q/dev-ui/extensions
### PhpMyAdmin
http://localhost:8080/
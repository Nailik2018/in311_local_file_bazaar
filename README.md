# Readme in311_local_file_bazaar
## Installation
```
Verwendete Versionen
java 21.0.2 2024-01-16 LTS
Java(TM) SE Runtime Environment (build 21.0.2+13-LTS-58)
Java HotSpot(TM) 64-Bit Server VM (build 21.0.2+13-LTS-58, mixed mode, sharing)

Docker version 24.0.6, build ed223bc
```
### Install docker Container
``` bash
docker-compose up -d
```
### .env Datei erstellen
```bash
javac ./src/main/java/ch/hftm/entity/EnvironmentFile.java
```

```bash
java -cp ./src/main/java ch.hftm.entity.EnvironmentFile
```

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
```
Benutername: blogger
Passwort: travel
```
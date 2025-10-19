# Aufgabenstellung 1.3: Geisternetze

## Kurzbeschreibung
Geisternetze ist eine Spring‑Boot‑Webanwendung zur Verwaltung von *Ghostnets* (Geisternetze melden, bergen oder als verschollen melden). Die Anwendung speichert Personendaten, Ghostnet‑Meldungen und monatliche Statistiken in einer MySQL‑Datenbank und bietet ein Web‑Frontend sowie eine REST‑Schnittstelle.

---
## Inhaltsverzeichnis
1. Projektüberblick / Was macht die Anwendung?  
2. Voraussetzungen  
3. Konfiguration vor dem Start (wichtige Hinweise)  
4. Schnellstart (Docker)  
5. Bauen & Starten ohne Docker  
6. Dev‑Modus (ohne MySQL)  


---
## 1. Projektüberblick / Was macht die Anwendung?
Die Anwendung verwaltet:
- Personen (Reporter / Retter)  
- Ghostnet‑Meldungen (Zeit, Koordinaten, Status, Reporter/Rescuer)  
- Monatsstatistiken (Tabelle `monthly_stat` mit Feldern für Berichtszahlen)

Technologien: Spring Boot, Spring Data JPA (Hibernate), Thymeleaf (Templates), MySQL (Produktiv), optional H2 (Dev). 

---
## 2. Voraussetzungen
- Java 17 (oder neuer) – Spring Boot 3.x benötigt mindestens JDK 17  
- Maven (z. B. Maven 3.8+) oder Maven Wrapper (`mvnw`)  
- Docker & docker‑compose (empfohlen für einfache DB‑Infrastruktur)  
- Optional: MySQL 8 (falls Sie Docker nicht verwenden möchten)

---
## 3. Konfiguration vor dem Start (Wichtig)
Im Repo befinden sich Beispiel‑Konfigurationsdateien im Ordner `src/main/resources` sowie eine `.env.template` im Projektroot. Bevor Sie die Anwendung starten, legen Sie bitte lokale Konfigurationsdateien an.

**Schritte (Beispiel):**
```bash
# Properties kopieren (Beispielkonfigurationen)
cp src/main/resources/application-mysql.properties.example src/main/resources/application-mysql.properties
cp src/main/resources/application.properties.example     src/main/resources/application.properties

# .env kopieren und anpassen
cp .env.template .env
# dann .env mit Editor öffnen und sichere Passwörter setzen
```

---
## 4. Schnellstart (Docker)
Die mitgelieferte `docker-compose.yml` startet eine MySQL‑Datenbank mit passenden Defaults (bzw. nimmt Werte aus `.env`).

1. Im Projekt‑Root starten:
```bash
# vorbereiten: .env anlegen (siehe oben)
docker-compose up -d
```

2. Prüfen, ob die DB läuft:
```bash
docker ps
docker logs geisternetze-db
```

3. Beispiel: Datenbank‑Query:
```bash
docker exec -it geisternetze-db mysql -uroot -p${MYSQL_ROOT_PASSWORD} geisternetze -e "SELECT * FROM monthly_stat;"
```


---
## 5. Bauen & Starten ohne Docker
Falls MySQL lokal läuft oder Docker bereits gestartet ist:

1. Projekt bauen:
```bash
mvn -DskipTests clean package
# oder (wenn vorhanden) mit Wrapper:
# ./mvnw -DskipTests clean package
```

2. Anwendung starten:
- Variante A (JAR, generischer Aufruf, da der Dateiname variieren kann):
```bash
java -jar target/*.jar --spring.profiles.active=mysql
```
- Variante B (aus dem Quellcode, ohne Paketierung):
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

3. Alternativ mit Umgebungsvariablen (Beispiel):
```bash
SPRING_DATASOURCE_URL="jdbc:mysql://127.0.0.1:3306/geisternetze?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8" \
SPRING_DATASOURCE_USERNAME=geisternetze \
SPRING_DATASOURCE_PASSWORD=<YOUR_DB_PASSWORD> \
java -jar target/*.jar --spring.profiles.active=mysql
```

Nach dem Start: Web‑UI öffnen unter `http://localhost:8080`

---
## 6. Dev‑Modus (ohne MySQL)
Wenn Sie keinen MySQL‑Server oder Docker nutzen möchten, bietet das Projekt ein `dev`‑Profil, das eine In‑Memory‑DB (H2) verwendet. Starten mit:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Aufgabenstellung 1.3 Geisternetze

Kurzbeschreibung
Geisternetze ist eine Spring-Boot-Webanwendung zur Verwaltung von “Ghostnets” (Geisternetz melden, bergen oder als verschollen melden). Die Anwendung speichert Personendaten, Ghostnet-Meldungen und monatliche Statistiken in einer MySQL-Datenbank und stellt ein Web-Frontend sowie eine REST-Schnittstelle zur Verfügung.

Inhaltsverzeichnis
	1.	Projektüberblick / Was macht die Anwendung?
	2.	Voraussetzungen
	3.	Schnellstart (Docker)
	4.	Bauen & Starten ohne Docker
	5.	Testen / Beispielnutzung


Projektüberblick / Was macht die Anwendung?

Die Anwendung verwaltet:
	•	Personen (Reporter / Retter)
	•	Ghostnet-Meldungen (Zeit, Koordinaten, Status, Reporter/Rescuer)
	•	Monatsstatistiken (Tabelle monthly_stat mit Feldern für Berichtszahlen)

Sie verwendet Spring Boot + Spring Data JPA (Hibernate) für DB-Zugriff und bietet ein Web-Frontend (Startseite / UI) sowie REST-Endpunkte für CRUD-Operationen. Ziel ist ein lauffähiges Projekt, das lokal oder per Docker reproduzierbar ist.

Voraussetzungen
	•	Java 17 (oder neuer) — Spring Boot 3.x setzt mindestens Java 17 voraus (language features & Laufzeit-API, Moduländerungen, Kompatibilität mit Bibliotheken wie Hibernate 6). Mindestens JDK 17 verwenden.
	•	Maven (zum Bauen aus dem Quellcode) — z. B. Maven 3.8+
	•	Docker & docker-compose (empfohlen für einfaches Setup der DB)
	•	Optional: MySQL 8 

Warum Java 17?
Spring Boot 3 und viele seiner Abhängigkeiten (z. B. Hibernate 6) nutzen Sprach-/Laufzeitfunktionen, die in Java 17 garantiert unterstützt werden. Mit älteren JDKs funktionieren Teile nicht und der Build/Start schlägt fehl.


Konfiguration vor dem Start (Wichtig: .example → echte Dateien anpassen)

Die im Repo enthaltenen Dateien application.properties.example und application-mysql.properties.example sind Beispiele. Bevor die Anwendung gestartet wird, kopieren Sie bitte die Beispiel-Dateien in echte Property-Dateien und passen bei Bedarf die Zugangsdaten an:
# Beispiel: Datei in den Ressourcen-Ordner kopieren

cp src/main/resources/application-mysql.properties.example src/main/resources/application-mysql.properties
cp src/main/resources/application.properties.example src/main/resources/application.properties

Öffnen Sie anschließend src/main/resources/application-mysql.properties und prüfen / ändern Sie die DB-Zugangsdaten (spring.datasource.url, spring.datasource.username, spring.datasource.password) so, dass sie zu Ihrer lokalen MySQL-Instanz bzw. zur docker-compose.yml passen.

Alternativ können die Datenbank-Zugangsdaten auch per Umgebungsvariablen übergeben werden (siehe application-mysql.properties mit Platzhaltern). Beispiel beim Start per Kommandozeile:
SPRING_DATASOURCE_URL="jdbc:mysql://127.0.0.1:3306/geisternetze?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8" \
SPRING_DATASOURCE_USERNAME=geisternetze \
SPRING_DATASOURCE_PASSWORD=change_me \
java -jar target/geisternetze-0.0.4-FINAL.jar --spring.profiles.active=mysql




Schnellstart (Docker)

Die mitgelieferte docker-compose.yml startet eine MySQL-Datenbank mit den richtigen Benutzer/DB-Einstellungen.
	1.	Im Projekt-Root (wo docker-compose.yml liegt) ausführen:
      docker-compose up -d

  2.	Prüfen, ob DB läuft:
      docker ps
      # oder
      docker logs <containername>

  Wozu dient die Datenbank geisternetze?
  Die Datenbank geisternetze enthält alle Tabellen der Anwendung (z. B. person, ghostnet, monthly_stat). Sie speichert die persistenten Daten, die die Anwendung im Betrieb nutzt.

	3.	App bauen (lokal) und starten — siehe nächsten Abschnitt. 

Bauen & Starten ohne Docker

Falls MySQL lokal läuft oder bereits Docker gestartet ist:

	1.	Build (im Projektroot):
      mvn -DskipTests clean package
  
  2.	App starten (MySQL muss erreichbar sein):
      java -jar target/geisternetze-0.0.4-FINAL.jar \
      --spring.profiles.active=mysql \
      --spring.datasource.url=jdbc:mysql://127.0.0.1:3306/geisternetze \
      --spring.datasource.username=geisternetze \
      --spring.datasource.password=change_me

    Danach im Browser öffnen:
    http://localhost:8080


Testen / Beispielnutzung
	•	http://localhost:8080 öffnen — das UI sollte geladen werden.
	•	Man kann Einträge anlegen (z. B. Personen, Ghostnets). Die Logs zeigen SQL-Statements (siehe z. B. insert into person (...) values (...)), das hilft beim Debuggen.
	•	Beispiel: die Anwendung schreibt monatliche Statistiken in die Tabelle monthly_stat. Man kann die DB direkt prüfen:
    # Mit docker container:
    docker exec -it geisternetze-db mysql -uroot -p123 geisternetze -e "SELECT * FROM monthly_stat;"
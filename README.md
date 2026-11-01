# GestionReservation

API REST de gestion d'événements et de réservations, développée avec **Spring Boot**.
Elle permet de gérer les catégories, les salles, les utilisateurs, les intervenants (speakers),
les événements et les réservations de places.

## Technologies

- Java 27 (à adapter dans `pom.xml` selon votre JDK, Java 17 minimum)
- Spring Boot 4.1.2 (Spring Web MVC, Spring Data JPA)
- Hibernate / JPA
- MySQL (production) ou H2 en mémoire (tests rapides)
- Maven (wrapper `mvnw` inclus)
- Postman pour tester l'API

## Structure du projet

```
src/main/java/com/example/demo
├── Entity/        Category, Venue, User, Speaker, Event, Reservation
├── repository/    interfaces JpaRepository + requêtes personnalisées
├── service/       logique métier et règles de gestion
├── controller/    endpoints REST (/api/...)
└── exception/     gestion des erreurs (404, 400, 409)
```

## Prérequis

- JDK installé (`java -version`) correspondant à `<java.version>` du `pom.xml`
- MySQL démarré (sauf si vous utilisez H2)

## Configuration

Le fichier `src/main/resources/application.properties` est à adapter.

### Option 1 : MySQL

```properties
spring.application.name=GestionReservation

spring.datasource.url=jdbc:mysql://localhost:3306/gestionreservation?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=VOTRE_MOT_DE_PASSE

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8081
```

La base `gestionreservation` est créée automatiquement. Les tables sont générées par Hibernate.

### Option 2 : H2 en mémoire (aucun mot de passe)

```properties
spring.application.name=GestionReservation

spring.datasource.url=jdbc:h2:mem:gestionreservation;DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8081
```

Les données sont perdues à chaque redémarrage.

> Ne publiez pas votre vrai mot de passe MySQL sur un dépôt public.

## Lancer l'application

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Ou depuis Spring Tool Suite : clic droit sur `GestionReservationApplication.java`
→ **Run As** → **Spring Boot App**.

L'application est prête quand la console affiche :

```
Tomcat started on port 8081
Started GestionReservationApplication
```

URL de base : `http://localhost:8081`

## Endpoints

### Catégories : `/api/categories`

| Méthode | URL | Description |
|---|---|---|
| POST | `/api/categories` | Créer une catégorie |
| GET | `/api/categories` | Lister |
| GET | `/api/categories/{id}` | Détail |
| PUT | `/api/categories/{id}` | Modifier |
| DELETE | `/api/categories/{id}` | Supprimer |

### Salles : `/api/venues`

| Méthode | URL | Description |
|---|---|---|
| POST | `/api/venues` | Créer une salle |
| GET | `/api/venues` | Lister |
| GET | `/api/venues/{id}` | Détail |
| PUT | `/api/venues/{id}` | Modifier |
| DELETE | `/api/venues/{id}` | Supprimer |
| GET | `/api/venues/search?city=Tunis&capacity=50` | Salles d'une ville avec capacité minimale |

### Utilisateurs : `/api/users`

| Méthode | URL | Description |
|---|---|---|
| POST | `/api/users` | Créer (201), email unique |
| GET | `/api/users` | Lister |
| GET | `/api/users/{id}` | Détail |
| PUT | `/api/users/{id}` | Modifier |
| DELETE | `/api/users/{id}` | Supprimer (204) |

### Speakers : `/api/speakers`

| Méthode | URL | Description |
|---|---|---|
| POST | `/api/speakers` | Créer |
| GET | `/api/speakers` | Lister |
| GET | `/api/speakers/{id}` | Détail |
| PUT | `/api/speakers/{id}` | Modifier |
| DELETE | `/api/speakers/{id}` | Supprimer |

### Événements : `/api/events`

| Méthode | URL | Description |
|---|---|---|
| POST | `/api/events` | Créer un événement |
| GET | `/api/events` | Lister |
| GET | `/api/events/{id}` | Détail |
| PUT | `/api/events/{id}` | Modifier (corps complet) |
| DELETE | `/api/events/{id}` | Supprimer |
| GET | `/api/events/a-venir?city=Tunis` | Événements à venir dans une ville |
| POST | `/api/events/{eventId}/speakers/{speakerId}` | Ajouter un speaker à un événement |

### Réservations : `/api/reservations`

| Méthode | URL | Description |
|---|---|---|
| POST | `/api/reservations` | Réserver des places (201) |
| GET | `/api/reservations` | Lister |
| GET | `/api/reservations/{id}` | Détail |
| PUT | `/api/reservations/{id}` | Modifier places et statut |
| DELETE | `/api/reservations/{id}` | Supprimer (204) |
| GET | `/api/reservations/event/{eventId}/remaining-seats` | Places restantes |
| GET | `/api/reservations/search?userId=1&start=2026-01-01T00:00:00&end=2027-12-31T23:59:59` | Réservations d'un utilisateur sur une période |

## Exemples de corps JSON

**Créer un événement** (`POST /api/events`) :

```json
{
  "title": "Spring Boot Day",
  "description": "Journée de démonstration",
  "startDate": "2027-01-15T10:00:00",
  "endDate": "2027-01-15T12:00:00",
  "capacity": 50,
  "category": { "id": 1 },
  "venue": { "id": 1 },
  "organizer": { "id": 1 },
  "speakers": [ { "id": 1 } ]
}
```

**Créer une réservation** (`POST /api/reservations`) :

```json
{
  "userId": 1,
  "eventId": 1,
  "seats": 2
}
```

**Modifier une réservation** (`PUT /api/reservations/{id}`) :

```json
{
  "seats": 3,
  "status": "CONFIRMED"
}
```

Statuts possibles : `PENDING`, `CONFIRMED`, `CANCELLED`.

> Les noms des champs sont sensibles à la casse : `userId` et `eventId`, pas `userID`.

## Règles de gestion

- La salle (`venue`) est obligatoire pour créer un événement.
- La date de fin doit être après la date de début.
- La capacité de l'événement doit être supérieure à 0 et ne pas dépasser celle de la salle.
- Une réservation est refusée (409) si le nombre de places demandé dépasse les places restantes.
- Les réservations annulées (`CANCELLED`) ne comptent pas dans les places occupées.
- L'email d'un utilisateur est unique (409 si déjà utilisé).

## Codes d'erreur

| Code | Signification |
|---|---|
| 200 / 201 / 204 | Succès |
| 400 | Règle métier non respectée ou données invalides |
| 404 | Ressource introuvable |
| 409 | Plus de places disponibles, ou email déjà utilisé |

Pour afficher le message détaillé dans les erreurs, ajoutez dans `application.properties` :

```properties
server.error.include-message=always
```

## Tester avec Postman

1. Ouvrir Postman → **Import** → choisir `GestionReservation.postman_collection.json`.
2. Vérifier la variable `baseUrl` de la collection (par défaut `http://localhost:8081`).
3. Clic sur les `...` de la collection → **Run collection** → **Run**.

La collection crée les données de test (catégorie, salle, utilisateur, speaker, événement),
puis teste les réservations et quelques cas d'erreur (409, 400, 404).

## Problèmes fréquents

| Problème | Cause et solution |
|---|---|
| `Access denied for user 'root'` | Mauvais mot de passe MySQL dans `application.properties` |
| `Unknown database` | Ajouter `createDatabaseIfNotExist=true` à l'URL JDBC |
| `Port 8081 was already in use` | Une autre instance tourne : l'arrêter (carré rouge de la console) ou changer `server.port` |
| `release version 27 not supported` | Mettre dans `pom.xml` un `<java.version>` égal ou inférieur à votre JDK |
| Postman : `Request failed` | L'application n'est pas démarrée |
| `404` sur `/api/reservations` | Ancienne instance encore active, ou user / event inexistants |

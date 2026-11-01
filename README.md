# Module Paiements & Avis

## 1. Contexte

Ce module fait partie de l'application de gestion d'événements (Spring Boot 3, Spring Data JPA).
Il gère :

- **le paiement** d'une réservation (un paiement par réservation) ;
- **les avis** laissés par les utilisateurs sur les événements auxquels ils ont participé ;
- des **statistiques** de notes (note moyenne par événement, classement par catégorie).

## 2. Entités

### Payment

| Champ         | Type            | Description                                   |
|---------------|-----------------|-----------------------------------------------|
| id            | Long            | Identifiant (auto-incrément)                  |
| amount        | BigDecimal      | Montant payé (> 0)                            |
| method        | PaymentMethod   | `CARTE`, `ESPECES`, `VIREMENT`, `D17`         |
| status        | PaymentStatus   | `EN_ATTENTE`, `PAYE`, `ECHOUE`, `REMBOURSE`   |
| paymentDate   | LocalDateTime   | Date du paiement (auto à la création)         |
| reservation   | Reservation     | **OneToOne** – FK `reservation_id` unique     |

### Review

| Champ     | Type          | Description                                  |
|-----------|---------------|----------------------------------------------|
| id        | Long          | Identifiant                                  |
| rating    | Integer       | Note de 1 à 5                                |
| comment   | String        | Commentaire (max 1000 caractères)            |
| createdAt | LocalDateTime | Date de création (auto)                      |
| user      | User          | **ManyToOne** – auteur de l'avis             |
| event     | Event         | **ManyToOne** – événement évalué             |

Contrainte unique `(user_id, event_id)` : un utilisateur ne donne qu'un avis par événement.

### Relations

```
Reservation 1 ──── 1 Payment
User        1 ──── * Review * ──── 1 Event
```

## 3. Règles métier

1. **Un avis seulement après participation** : l'utilisateur doit avoir une réservation dont le
   paiement est au statut `PAYE` (réservation non annulée), et l'événement doit avoir commencé. Sinon → `400 Bad Request`.
2. Un seul avis par utilisateur et par événement.
3. Une réservation ne peut avoir qu'un seul paiement, et une réservation `CANCELLED` ne peut pas être payée.
4. L'auteur et l'événement d'un avis ne sont pas modifiables (seuls note et commentaire).

## 4. Endpoints

Base URL : `http://localhost:8080`

### Paiements – `/api/payments`

| Méthode | URL                                   | Description                    |
|---------|---------------------------------------|--------------------------------|
| POST    | `/api/payments`                       | Créer un paiement              |
| GET     | `/api/payments`                       | Lister les paiements           |
| GET     | `/api/payments/{id}`                  | Détail d'un paiement           |
| GET     | `/api/payments/reservation/{resId}`   | Paiement d'une réservation     |
| PUT     | `/api/payments/{id}`                  | Modifier un paiement           |
| DELETE  | `/api/payments/{id}`                  | Supprimer un paiement          |

Exemple de corps :

```json
{ "amount": 120.0, "method": "CARTE", "status": "PAYE", "reservationId": 1 }
```

### Avis – `/api/reviews`

| Méthode | URL                              | Description                  |
|---------|----------------------------------|------------------------------|
| POST    | `/api/reviews`                   | Créer un avis                |
| GET     | `/api/reviews`                   | Lister les avis              |
| GET     | `/api/reviews/{id}`              | Détail d'un avis             |
| GET     | `/api/reviews/event/{eventId}`   | Avis d'un événement          |
| GET     | `/api/reviews/user/{userId}`     | Avis d'un utilisateur        |
| PUT     | `/api/reviews/{id}`              | Modifier un avis             |
| DELETE  | `/api/reviews/{id}`              | Supprimer un avis            |

```json
{ "rating": 5, "comment": "Super événement !", "userId": 1, "eventId": 1 }
```

### Requêtes personnalisées

| Méthode | URL                                        | Description                                         |
|---------|--------------------------------------------|-----------------------------------------------------|
| GET     | `/api/reviews/stats/category/{categoryId}` | Événements d'une catégorie triés par note moyenne   |
| GET     | `/api/reviews/stats/event/{eventId}`       | Note moyenne d'un événement                         |
| GET     | `/api/reviews/stats/events`                | Note moyenne de tous les événements notés           |

Réponse :

```json
[
  { "eventId": 3, "eventTitle": "Festival Jazz", "averageRating": 4.67, "reviewCount": 3 },
  { "eventId": 1, "eventTitle": "Conférence IA",  "averageRating": 4.0,  "reviewCount": 2 }
]
```

## 5. Structure

```
src/main/java/com/example/demo/
├── controller/   PaymentController, ReviewController
├── dto/          PaymentRequest/Response, ReviewRequest/Response, EventRatingDTO
├── Entity/       Payment, Review, enums/PaymentMethod, enums/PaymentStatus
├── exception/    ResourceNotFoundException, BusinessException
├── repository/   PaymentRepository, ReviewRepository
└── service/      PaymentService, ReviewService
postman/          Paiements-Avis.postman_collection.json
docs/captures/    Captures d'écran Postman
```

## 6. Instructions

1. **Prérequis** : Java 17+, Maven, base de données configurée dans `application.properties`.
2. Dépendances nécessaires dans `pom.xml` : `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
   driver SQL. (La validation des données est faite dans les services, sans dépendance supplémentaire.)
3. Le module s'appuie sur les entités existantes `User`, `Event`, `Reservation`, `Category` et leurs repositories.
   Champs utilisés : `Event.title`, `Event.category`, `Event.startDate`, `Reservation.user`,
   `Reservation.event`, `Reservation.status`.
4. (Optionnel) côté `Reservation`, relation bidirectionnelle :
   ```java
   @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonIgnore
   private Payment payment;
   ```
5. Lancer : `mvn spring-boot:run`
6. **Postman** : *Import* → `postman/Paiements-Avis.postman_collection.json`. La variable `baseUrl`
   vaut `http://localhost:8080`. Ordre de test conseillé : créer un paiement `PAYE` → créer un avis →
   statistiques → tester la règle métier (avis sans participation → 400).

## 7. Captures Postman

| Test | Capture |
|------|---------|
| Créer un paiement | ![](docs/captures/01-create-payment.png) |
| Lister les paiements | ![](docs/captures/02-list-payments.png) |
| Créer un avis | ![](docs/captures/03-create-review.png) |
| Avis refusé (règle métier) | ![](docs/captures/04-review-refused.png) |
| Événements d'une catégorie triés par note | ![](docs/captures/05-category-ranking.png) |
| Note moyenne d'un événement | ![](docs/captures/06-event-average.png) |

> Placez vos captures dans `docs/captures/` avec ces noms de fichiers.

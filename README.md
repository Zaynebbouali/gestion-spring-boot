# GestionReservation

## 📌 Description

**GestionReservation** est une application web développée avec **Spring Boot** permettant de gérer des événements, des utilisateurs, des salles, des réservations, des paiements, des intervenants et des avis.

L'application utilise une architecture REST avec **Spring Boot, Spring Data JPA, Hibernate et MySQL**.

---

## 🛠️ Technologies utilisées

- Java
- Spring Boot
- Spring Data JPA
- Hibernate / JPA
- MySQL
- Maven
- Postman
- Git / GitHub

---

## 📂 Fonctionnalités

L'application permet de gérer :

- Les catégories
- Les salles (venues)
- Les utilisateurs
- Les événements
- Les intervenants (speakers)
- Les réservations
- Les paiements
- Les avis (reviews)

---

## 👥 Répartition des tâches

### 👩‍💻 Membre 1 + Membre 3

#### Gestion des référentiels et réservations

Entités :

- `Category`
- `Venue`
- `User`
- `Reservation`

Fonctionnalités :

- CRUD complet des catégories
- CRUD complet des salles
- CRUD complet des utilisateurs
- CRUD complet des réservations
- Recherche des salles par ville et capacité minimale
- Gestion des relations entre les différentes entités

---

### 👨‍💻 Membre 3

#### Gestion des événements

Entités :

- `Event`
- `Speaker`

Fonctionnalités :

- CRUD complet des événements
- CRUD complet des speakers
- Relation entre `Event` et `Category`
- Relation entre `Event` et `Venue`
- Relation entre `Event` et `User` (organizer)
- Relation ManyToMany entre `Event` et `Speaker`
- Recherche des événements à venir par ville
- Top 5 des événements les plus réservés
- Vérification de la date de fin par rapport à la date de début
- Vérification de la capacité de la salle

---

### 👩‍💻 Membre 4

#### Gestion des paiements et avis

Entités :

- `Paiement`
- `Review`

Fonctionnalités :

- CRUD complet des paiements
- CRUD complet des avis
- Gestion des relations avec les utilisateurs et les réservations
- Gestion des informations liées aux paiements
- Gestion et consultation des avis

---

## 🗄️ Base de données

Le projet utilise **MySQL** comme système de gestion de base de données.

La configuration de la base de données se trouve dans :

```text
src/main/resources/application.properties
```

Exemple :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestionreservation
spring.datasource.username=root
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 🏗️ Architecture du projet

Le projet suit une architecture en couches :

```text
src/main/java
└── com.example.demo
    ├── entity
    ├── repository
    ├── service
    └── controller
```

### Entity

Contient les entités JPA :

```text
Category
Venue
User
Reservation
Event
Speaker
Paiement
Review
```

### Repository

Permet l'accès aux données avec **Spring Data JPA**.

### Service

Contient la logique métier de l'application.

### Controller

Expose les API REST permettant de communiquer avec l'application.

---

## 🔎 Exemple de requête personnalisée

Recherche des salles par ville et capacité minimale :

```java
List<Venue> findByCityAndCapacityGreaterThanEqual(
    String city,
    Integer capacity
);
```

---

## 🧪 Tests avec Postman

Les API REST peuvent être testées avec **Postman**.

Les principales opérations HTTP utilisées sont :

| Méthode | Utilisation |
|---|---|
| GET | Consulter les données |
| GET /{id} | Consulter une donnée par ID |
| POST | Ajouter une donnée |
| PUT | Modifier une donnée |
| DELETE | Supprimer une donnée |

Une collection Postman est utilisée pour tester les différentes fonctionnalités de l'application.

---

## 🚀 Installation

### 1. Cloner le projet

```bash
git clone https://github.com/Zaynebbouali/gestion-spring-boot.git
```

### 2. Ouvrir le projet

Importer le projet dans **Spring Tool Suite (STS)** ou **Eclipse**.

### 3. Créer la base de données

Dans MySQL :

```sql
CREATE DATABASE gestionreservation;
```

### 4. Configurer la connexion

Modifier le fichier :

```text
src/main/resources/application.properties
```

### 5. Lancer l'application

Exécuter :

```text
GestionReservationApplication.java
```

L'application démarre généralement sur :

```text
http://localhost:8081
```

---

## 📌 État du projet

Projet réalisé en équipe dans le cadre d'un projet Spring Boot.

Le développement est effectué avec **Git et GitHub** afin de faciliter le travail collaboratif, la gestion des branches et l'intégration des différentes fonctionnalités.

## 👥 Équipe

- Membre 1+3 — Category, Venue, User, Reservation
- Membre 2 —  Event, Speaker
- Membre 4 — Paiement, Review

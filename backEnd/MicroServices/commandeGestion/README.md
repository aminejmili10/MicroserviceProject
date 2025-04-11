# Microservice CommandeGestion

## Aperçu

Le microservice CommandeGestion fait partie intégrante de la plateforme e-commerce construite en architecture de microservices. Il est responsable de la création, de la gestion et du suivi des commandes passées par les utilisateurs. Ce service joue un rôle crucial dans le processus d'achat en coordonnant les actions entre les services de produits, paiement, et livraison.
### Rôle dans la Plateforme E-commerce

- **Objectif** : Gérer les commandes des utilisateurs.
- **Responsabilités principales** :
    - Créer de nouvelles commandes à partir des articles sélectionnés.
    - Suivre l’état des commandes (en cours, livrée, annulée…).
    - Coordonner avec les services Paiement et Livraison.
    - Supprimer des commandes du catalogue.
- **Interactions** :
    - Interagit avec le ProductGestion pour récupérer les détails des produits.
    - Communique avec le Service Paiement pour le traitement des transactions.
    - S’interface avec le Service Livraison pour organiser l’envoi des commandes.

## Structure du Projet

Le microservice ProductGestion est construit avec **Spring Boot** et suit une architecture en couches :

- **Couche Entité** :Contient l'entité Order (commande) avec des attributs comme id, date, produits, statut, total, methodePaiement, etc.
- **Couche Repository** : Interface OrderRepository étendant Spring Data JPA pour les opérations en base de données.
- **Couche Service** :  Classe OrderService implémentant la logique métier : création de commande, calcul du total, gestion du statut, etc.
- **Couche Contrôleur** : OrderRestController expose les endpoints REST ( /orders, /orders/{id}, /orders/user/{id}).

## Technologies Utilisées

- **Java Spring Boot** : Framework backend pour construire le microservice.
- **Spring Data JPA** : Pour les opérations sur la base de données.
- **MySQL** : Base de données pour stocker les données des produits.
- **Lombok** : Pour réduire le code répétitif (par exemple, getters, setters, constructeurs).
- **Spring Cloud** : Pour la découverte de services (Eureka) et la gestion de la configuration (Config Server).
- **Maven** : Gestion des dépendances et outil de construction.

## Prérequis

Avant de lancer le microservice ProductGestion, assurez-vous que les éléments suivants sont installés :

- **Java 17** : Le projet est construit avec Java 17.
- **Maven** : Pour la gestion des dépendances et la construction du projet.
- **MySQL** : Pour la base de données (en cours d'exécution sur `localhost:3306`).
- **Eureka Server** : Pour la découverte de services (en cours d'exécution sur `http://localhost:8761`).
- **Config Server** : Pour la gestion centralisée de la configuration (en cours d'exécution sur `http://localhost:8888`).
- **Docker** (optionnel) : Si vous préférez exécuter le service dans un conteneur.

## Installation et Configuration

1. **Cloner le Dépôt** :
   Clonez le dépôt principal du projet (si ce n'est pas déjà fait) :
   ```bash
   git clone https://github.com/aminejmili10/MicroserviceProject.git
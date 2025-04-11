Aperçu
Le microservice Paiement fait partie intégrante de la plateforme e-commerce basée sur une architecture de microservices. Ce service est dédié à la gestion des paiements des commandes passées par les utilisateurs. Il permet de valider les transactions, de générer automatiquement les factures associées et de suivre l’état des paiements.

Rôle dans la Plateforme E-commerce
Objectif : Gérer les paiements en ligne liés aux commandes client.

Responsabilités principales :

Traiter les paiements à la suite d'une commande.

Générer automatiquement une facture après un paiement validé.

Fournir l’historique des paiements pour chaque commande.

Mettre à jour le statut des paiements (EN_ATTENTE, VALIDÉ, ÉCHOUÉ).

Interactions :

Interagit avec le Service Commande pour récupérer les détails de la commande liée au paiement (relation One-to-One).

Interagit avec le Service Facture pour générer une facture une fois le paiement confirmé (relation One-to-One).

Structure du Projet
Le microservice Paiement est construit avec Spring Boot et suit une architecture en couches :

Couche Entité : Contient les entités Paiement et Facture avec des champs comme id, commandeId, montant, datePaiement, statut, dateFacture, etc.

Couche Repository : Utilise Spring Data JPA (PaiementRepository, FactureRepository) pour accéder à la base de données.

Couche Service : Implémente la logique métier dans PaiementService et FactureService, incluant la validation du paiement et la création de factures.

Couche Contrôleur : Expose des API REST via PaiementRestController pour initier un paiement, récupérer les détails ou accéder à une facture.

Technologies Utilisées
Java Spring Boot : Framework backend principal du microservice.

Spring Data JPA : Pour les opérations CRUD sur les entités Paiement et Facture.

MySQL : Base de données relationnelle pour stocker les données de paiement.

Lombok : Pour automatiser la génération de getters/setters/constructeurs.

Spring Cloud : Pour la découverte (Eureka) et la configuration (Config Server).

Maven : Pour la gestion des dépendances et la compilation du projet.

RabbitMQ ou Kafka (optionnel) : Pour la notification asynchrone du traitement de paiement.

Prérequis
Avant de lancer le microservice Paiement, assurez-vous d’avoir les éléments suivants installés :

Java 17 : Le microservice utilise Java 17.

Maven : Pour compiler le projet et gérer les dépendances.

MySQL : Pour la persistance des données (par défaut sur localhost:3306).

Eureka Server : Pour la découverte des services (exécuté sur http://localhost:8761).

Config Server : Pour charger les configurations centralisées (sur http://localhost:8888).

Docker (optionnel) : Pour une exécution conteneurisée du service.
Microservice DemandeGestion
Aperçu
Le microservice DemandeGestion est un composant essentiel de la plateforme e-commerce basée sur une architecture de microservices. Il est conçu pour gérer les demandes des clients, telles que les retours, réclamations, demandes d’information, échanges et annulations de commandes. Ce service améliore l’expérience utilisateur en offrant une gestion centralisée et efficace des requêtes post-achat.

Rôle dans la Plateforme E-commerce
Objectif : Faciliter la gestion des demandes client liées aux commandes et produits.
Responsabilités principales :
Enregistrer de nouvelles demandes (par exemple, un retour ou une réclamation).
Récupérer les détails des demandes pour suivi ou traitement.
Mettre à jour les informations des demandes (par exemple, date de traitement).
Supprimer des demandes si nécessaire.
Interactions :
Collabore avec le Service Commande pour associer les demandes à des commandes spécifiques (relation potentielle One-to-Many).
Interagit avec le Service Produit pour valider les informations des produits liés aux demandes.
Structure du Projet
Le microservice DemandeGestion est construit avec Spring Boot et suit une architecture en couches :

Couche Entité : Définit l’entité Demande avec les champs id, datedemande, datetraitement, et typeDemande (une énumération : RETOUR, RECLAMATION, INFORMATION, ECHANGE, ANNULATION).
Couche Repository : Utilise Spring Data JPA (DemandeRepository) pour les opérations CRUD sur la base de données MySQL.
Couche Service : Implémente la logique métier dans DemandeService, qui fournit des méthodes comme ajouterDemande via l’interface IDemandeService.
Couche Contrôleur : Expose des endpoints RESTful via DemandeRestController, tels que POST /Demande/add pour ajouter une demande.
Technologies Utilisées
Java Spring Boot : Framework backend principal.
Spring Data JPA : Gestion des interactions avec la base de données.
MySQL : Stockage des données des demandes.
Lombok : Réduction du code répétitif (getters, setters, etc.).
Spring Cloud : Intégration avec Eureka pour la découverte de services.
SLF4J : Journalisation des opérations.
Maven : Gestion des dépendances et construction.
Prérequis
Avant de lancer DemandeGestion, assurez-vous que les éléments suivants sont en place :

Java 17 : Version utilisée pour le développement.
Maven : Pour gérer les dépendances et construire le projet.
MySQL : Base de données exécutée sur localhost:3306.
Eureka Server : Serveur de découverte actif sur http://localhost:8761/eureka.
Docker (optionnel) : Pour une exécution conteneurisée.
Installation et Configuration:
git clone https://github.com/aminejmili10/MicroserviceProject.git
cd MicroserviceProject/backEnd/MicroServices/demandeGestion
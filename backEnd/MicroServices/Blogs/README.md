# Mini-Projet Gestion de Blog - Architecture Microservices

##  Description

Ce projet fait partie de la plateforme globale de gestion de projets dans le domaine du BTP, et représente le **module de blog**. Il est conçu selon une architecture **microservices**. Ce service permet de publier, gérer, commenter et modérer des articles techniques en rapport avec les projets de construction. Ce module vise à **renforcer l’engagement des utilisateurs**, à **diffuser des connaissances techniques** et à **favoriser la collaboration** entre les acteurs du projet.



## Architecture de l'Application

Voici les schémas clés liés à ce microservice :

### Architecture
> _Diagramme représentant la communication entre le service Blog et les autres microservices comme Utilisateur et Notification._

### Diagramme de Classe
> _Diagramme des entités `Post`, `Comment`, et `User` avec leurs relations._



##  Microservices Impliqués

###  Service Blog :
- Création, modification et suppression des articles de blog.
- Gestion des brouillons et planification de publication.
- Résumé automatique et lecture audio des articles.
- Affichage en temps réel des nouveaux posts et commentaires (WebSockets).
- Modération des commentaires signalés.

### Service Utilisateur :
- Authentification et attribution des rôles (auteur, lecteur, admin).
- Interaction entre les articles/commentaires et les comptes utilisateurs.

###  Service Notification (interne) :
- Notifications WebSocket envoyées à l’admin en cas de nouveau post ou commentaire.



##  Interactions entre Microservices

- `Post` → `User` : chaque article est écrit par un utilisateur (Many-to-One).
- `Comment` → `Post` : un article peut avoir plusieurs commentaires (One-to-Many).
- `Comment` → `User` : chaque commentaire appartient à un utilisateur.
- `Post` → `Notification` : déclenchement d’une notification en temps réel (WebSocket).
- `Comment` (signalé) → `Admin View` : visibilité côté admin uniquement pour modération.



##  Diagramme de Classes Global

Ce diagramme modélise les entités :
- `Post` (titre, contenu, résumé, date, brouillon, statut)
- `Comment` (contenu, auteur, date, état)
- `User` (nom, rôle)
- Et leurs relations.



##  Objectifs du Projet

- Développer un système de blog avancé et modulaire.
- Intégrer des fonctionnalités modernes (lecture audio, résumé automatique, modération).
- Faciliter la maintenance grâce à une architecture microservices.
- Encourager la contribution et l’engagement des utilisateurs via des commentaires enrichis.



## ⚙ Installation

Clonez ce repository :
```bash
git clone https://github.com/aminejmili10/MicroserviceProject.git
🛠 Technologies Utilisées
Spring Boot pour les microservices backend.

Angular 16 pour l’interface utilisateur.

MySQL pour la persistance des données.

WebSockets (Spring) pour les notifications en temps réel.

Jsoup pour le scraping (futur ajout IA).

Docker pour l’orchestration des services.
 Contribution
Les contributions sont les bienvenues !

Forkez le repo

Créez une branche : git checkout -b feature/ma-nouvelle-fonctionnalite

Commitez vos modifications : git commit -m "Ajout de la modération des commentaires"

Poussez : git push origin feature/ma-nouvelle-fonctionnalite

Créez une Pull Request
 Contact
Pour toute question, contactez aminejmili10 sur GitHub.




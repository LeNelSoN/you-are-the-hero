![Couverture](https://i.imgur.com/PhHGK8G.jpg)

# L'API Dont Vous Êtes le Héros

## Description
Ce projet est une API en Java qui ravive la magie des vieux livres "dont vous êtes le héros", que l'on trouve parfois dans les brocantes et les librairies d'occasion. Ces livres emblématiques ont bercé notre enfance en nous permettant de choisir notre propre aventure à chaque page. Avec cette API, je souhaite recréer cette expérience immersive et interactive, où les utilisateurs peuvent naviguer à travers des histoires captivantes et prendre des décisions qui influencent le déroulement de leur aventure. Plongez dans un monde de choix et d'imagination, tout en redécouvrant la nostalgie des récits qui ont marqué des générations !

## Fonctionnalités
- Démarrer une nouvelle aventure.
- Choisir des actions à chaque étape.
- Crée des histoires et ajouter des scénes
- Authentification avec JWT pour la gestion des sessions.

## Technologies Utilisées
Ce projet repose sur plusieurs technologies modernes pour garantir une performance et une sécurité optimales :

- **Java** : Le langage de programmation utilisé pour développer l'API, offrant une robustesse et une évolutivité.
- **Spring Boot** : Framework Java qui facilite le développement d'applications en réduisant la configuration nécessaire et en offrant des outils puissants pour la création d'APIs.
- **JWT (JSON Web Token)** : Utilisé pour l'authentification sécurisée des utilisateurs, permettant de gérer les sessions sans avoir à stocker d'informations sensibles côté serveur.
- **MongoDB** : Base de données NoSQL choisie pour sa flexibilité et sa capacité à gérer des documents de manière dynamique, ce qui convient parfaitement à la nature évolutive des histoires interactives.

## HATEOAS
Cette API intègre le principe de HATEOAS, ce qui signifie que chaque réponse inclut des liens hypermédia vers d'autres actions possibles. Cela permet aux clients d'interagir avec l'API de manière dynamique, sans avoir besoin de connaître à l'avance toutes les URL disponibles. Par exemple, lorsqu'un utilisateur démarre une nouvelle aventure, la réponse inclura des liens vers les choix disponibles, le suivi de la progression et d'autres fonctionnalités pertinentes. Cette approche favorise une navigation intuitive et facilite l'intégration de nouveaux utilisateurs en rendant la documentation moins cruciale.

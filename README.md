# CYProjetGL_2025

Un remake classique de Pac-Man développé en Java avec Swing. Le joueur incarne Pac-Man et doit éviter les fantômes tout en mangeant toutes les pac-gommes du labyrinthe.

## 🎮 Fonctionnalités

- Déplacement fluide de Pac-Man via les touches directionnelles ou ZQSD.
- IA personnalisée pour chaque fantôme :
  - 👻 **Red Ghost (Blinky)** : suit directement Pac-Man.
  - 👻 **Pink Ghost (Pinky)** : anticipe la position de Pac-Man.
  - 👻 **Blue Ghost (Inky)** : poursuit Pac-Man ou s’éloigne selon la distance.
  - 👻 **Orange Ghost (Clyde)** : se déplace aléatoirement.
- Apparition de fruits bonus selon le niveau.
- Mode « effrayé » : les fantômes deviennent vulnérables quand Pacman mange une super pac-gomme.
- Affichage graphique, menu principal et tableaux des scores avec Swing.
- Architecture orientée objet respectant l'encapsulation.

## Marche à suivre pour lancer le jeu
Si vous voulez executer le jeu localement en téléchargeant le code sur votre machine (il faut avoir maven d'installé sur votre machine):
  - Tapez dans un invite de commande ouvert dans le dossier du projet, "Pacman3.0" : "mvn clean compile" puis "mvn exec:java"

Si vous voulez simplement lancer grâce à l'executable :
  - En double-cliquant dur le .jar il se peut que la musique ne se lance pas mais le jeu fonctionnera normalement
  - Si vous voulez lancer le jeu avec la musique de fond, vous pouvez directement taper dans un invite de commande ouvert dans le dossier du projet : "java -jar target/pacman-2.0-SNAPSHOT.jar"

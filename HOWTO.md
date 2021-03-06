# Circle Messenger

## Manuel d'utilisation

L'application Circle Messenger est disponible sur Android à partir de la version 6.0 (Marshmallow).

### Installation et lancement de l'application

* Installer l'APK ;
* Lancer l'application ;
* Accepter les demandes d'autorisation :
  * Accéder à vos contacts ;
  * Envoyer et consulter les SMS ;
  * Effectuer et gérer des appels téléphoniques ;

### Utilisation de l'application

Au lancement de l'application, celle-ci va charger vos contacts avec numéros de téléphone, les SMS et le journal d'appels, ce qui prend un certain temps (une bonne dizaine de secondes parfois). Elle va de même charger les données locales enregistrées lors des utilisations précédentes (les types des contacts définis).
L'application montre vos différents contacts avec le premier numéro de téléphone enregistré, le type de contact (None par défaut, Ami, Famille ou Collègue) et deux images :
* La triforce, permettant de demander au serveur de "typer" un contact ;
* Une image représentant le type de contact.
Tout en haut de l'application, en position fixe, se trouvent deux boutons : export et import.

#### Export

Le bouton d'export permet d'envoyer au serveur les contacts sous la forme de vecteurs. Cette fonctionnalité a deux utilités :
* Garder sur le serveur les types de nos contacts ;
* Lancer l'algorithme d'apprentissage sur le serveur.

#### Import

Si vous avez au préalable exporté vos contacts, vous pouvez, même après avoir désinstallé puis réinstallé l'application, récupérer les types des contacts sur celle-ci.
En cliquant sur le bouton "Import", l'application va appeler le serveur qui va retourner tous les contacts de votre téléphone, typés et exportés au préalable, et les associer dans l'application.

## Consignes d'installation

Pour l'application, nous utilisons un serveur distant, http://colombet-aoechat.rhcloud.com . Si vous souhaitez utiliser un autre serveur, il vous faudra modifier le code : remplacer la valeur de la variable "ipServer" dans la classe fr.unice.polytech.iam.utils.Macumba.
Pour l'installation du serveur, il vous suffira de lancer le script install.sh en sudo, dans le dossier script (testé dans une VM Ubuntu 16.04). Cela va installer tout se qu'il faut pour faire marcher le serveur. Ce script va probablement vous demander si vous voulez installer les packages, repondez Oui à tous. L'installation de MySQL va vous demander de mettre un nom d'utilisateur et mot de passe, rentrez les valeurs : root root (login et mot de passe). Si vous êtes sur une VM, vérifiez bien que vous êtes en "accès par pont" dans les réglages du réseau et connecté sur le même réseau que votre téléphone.
Ensuite, il suffira de faire un ifconfig et récupérer l'ip du serveur pour la mettre dans la variable citée ci-dessus dans les sources android. Enfin, il faudra compiler le code android et donc de lancer l'application pour la tester.

sudo apt install apache2 php mysql-server libapache2-mod-php php-mysql #installation apache php mysql

sudo apt install r-base-core #installation de R

sudo rm -rf /var/www/html/* #On supprime tout se qu'il y a de base dans l'install de base d'apache....

cp -R ../sources/server/* /var/www/html/ #on copie les sources du serveur ...

sudo Rscript installLibs.r #on install la lib random forest de R

sudo service apache2 restart # on restart apache ...

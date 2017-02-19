sudo apt install apache2 php mysql-server libapache2-mod-php php-mysql

sudo apt install r-base-core

sudo rm -rf /var/www/html/*

cp -R ../sources/server/* /var/www/html/


cd /var/www/html

export OPENSHIFT_DATA_DIR=/var/www/html/data/




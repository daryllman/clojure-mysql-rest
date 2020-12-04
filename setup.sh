sudo apt update && sudo apt upgrade -y
sudo apt install unzip
sudo apt install -y openjdk-8-jre-headless
sudo apt install -y leiningen

wget -c https://istd50043.s3-ap-southeast-1.amazonaws.com/kindle-reviews.zip -O kindle-reviews.zip
unzip kindle-reviews.zip
rm -rf kindle_reviews.json

sudo apt-get -y install mysql-server
sudo mysql -u root
sudo mysql -e 'update mysql.user set plugin = "mysql_native_password" where user="root"'
sudo mysql -e 'create user "root"@"%" identified by ""'
sudo mysql -e 'grant all privileges on *.* to "root"@"%" with grant option'
sudo mysql -e 'flush privileges'
sudo service mysql restart

sudo mysql -e 'create database `kindle-reviews`'
sudo mysql -u root "kindle-reviews" < "dbsetup.sql"
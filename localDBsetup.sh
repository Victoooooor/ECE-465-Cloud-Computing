#!/usr/bin/env bash

echo "Checking if MySQL server version..."
mysql --version

# running mysql script to setup database tables
echo "Setting up tables..."

echo "Please enter the root password for mySQL server"
mysql -u root -p < sql_script/a.sql

echo "Done."

# running mysql script to grant privileges
echo "Granting privileges..."

echo "Please enter the root password for mySQL server"
mysql -u root -p < sql_script/b.sql

echo "Done."

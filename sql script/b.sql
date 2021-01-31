SET GLOBAL max_allowed_packet=4294967296;
DROP USER IF EXISTS 'user1'@'localhost';
CREATE USER 'user1'@'%' IDENTIFIED BY 'PASSWORD';
GRANT USAGE ON *.* TO 'user1'@'%';
GRANT ALL privileges ON *.* TO 'user1'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;

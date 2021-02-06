# ECE-465-Cloud-Computing
Cooper Union ECE-465 Spring 2021
<<<<<<< Updated upstream
=======

##To run Threade File Storage:
###run a.sql and b.sql under "./sql script" folder for mariadb to setup database and user w/ privileges

###Then run script.sh under "./testfile" which randomly generate small files for testing

###Run ece465.threaded_store_hash_test.java and ece465.single_store_test.java,
###the speed difference should be quite noticable
###Run ece465.search_test.java in with 1 thread and 50 threads,
###the speed difference should be quite noticable


##to compile do command:
###mvn clean package
##to run:
###java -jar target/BlockChain_FileSystem.jar


##MVP2: added fetching from database, hash and logger, also fixed maven pom setup


# ECE-465-Cloud-Computing
Cooper Union ECE-465 Spring 2021

Pre-requsite:
    - MariaDB (version 10.5 or newer)
    - Maven
    
Database setup:
    Make sure you cd into the project folder
    
    To setup the test databse, run
        > mysql -u root -p < sql_script\a.sql
    To setup user with privileges, run
        > mysql -u root -p < sql_script\b.sql
        
Compilation
    We use Maven to compile our project:
        > mvn clean package
    this should download all the dependencies needed and pack everything in a jar file
    
Execution
    To execute our program, make sure you are in the project folder and run the following command in command line:
    
        This will store every file in the currnet directory into test database running on one single thread
        > java -cp target\BlockChain_FileSystem.jar ece465.single_store_test
        
        This will store every file in the currnet directory into test database running on multiple threads
        > java -cp target\BlockChain_FileSystem.jar ece465.threaded_store_test
        
        This will search for files containing a specific search word:
        > java -cp target\BlockChain_FileSystem.jar ece465.search_test
            > java -jar target\BlockChain_FileSystem.jar            # note: this will do the same thing
    

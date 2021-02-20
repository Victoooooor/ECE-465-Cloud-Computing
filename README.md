Cooper Union ECE-465 Spring 2021

Pre-requsite: - MariaDB (version 10.5 or newer) - Maven

Database setup: Make sure you cd into the project folder

To setup the test databse, run
    > mysql -u root -p < sql_script\a.sql
To setup user with privileges, run
    > mysql -u root -p < sql_script\b.sql
Compilation We use Maven to compile our project: 

     > mvn clean package 

   this should download all the dependencies needed and pack everything in a jar file

Execution To execute our program, make sure you are in the project folder and run the following command in command line:

    To generage some random small files for testing, run:
    > ./testfile/script.sh
    
    This will store every file in the currnet directory into test database running on one single thread
    > java -cp target\BlockChain_FileSystem.jar ece465.single_store_test
    
    This will store every file in the currnet directory into test database running on multiple threads
    > java -cp target\BlockChain_FileSystem.jar ece465.threaded_store_test
    
    This will search for files containing a specific search word:
    > java -cp target\BlockChain_FileSystem.jar ece465.search_test

    This will search for files containing a specific search word and download them into "./temp" (will create such directory if not already exisits)
    > java -cp target\BlockChain_FileSystem.jar ece465.fetch_test
    
    This will store every file in the current directory into test database, and hash the files to store in the blocks table
    > java -cp target\BlockChain_FileSystem.jar ece465.threaded_store_hash_test
      > java -jar target\BlockChain_FileSystem.jar    # note: this will do the same
      
    To test working server and client:
        Run server_test first by:
            > java -cp target\BlockChain_FileSystem.jar ece465.server
        Then run client_test (multiple times if needed) by:
            > java -cp  target\BlockChain_FileSystem.jar ece465.client
            
    testJson will create a retrieve Json string and read it, and create a retrieve return Json and read it.
    > java -cp target\BlockChain_FileSystem.jar ece465.testJson


##MVP3: implemented peer nodes (client and server), read and write JSON to be passed around.
    To-do: 
        implement network and protocol, consensus and leader election.

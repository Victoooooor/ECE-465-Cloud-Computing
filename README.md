# Distributed File System
Cooper Union ECE-465 Spring 2021

![alt text](https://github.com/Victoooooor/ECE-465-Cloud-Computing/blob/main/ECE465_MVP4.png?raw=true)

    Shown above is a simple demostration of our distributed network. Each node server is connected to at least two other nodes, and a peer list of the nodes that it is connected to is maintained within each node. 

    When a new node server is added to this network, it will need an entry node IP address. The new node is registered to the entry point and its registration will be broadcasted to other nodes (to be implemented) so that the new node is connected to more than one nodes for redundancy purposes. 

Pre-requsite: 

    - MariaDB (version 10.5 or newer) 
    - Maven

Database setup: Make sure you cd into the project folder

    To setup the test databse, run
    > mysql -u root -p < sql_script\a.sql
    To setup user with privileges, run
    > mysql -u root -p < sql_script\b.sql
Compilation We use Maven to compile our project: 

     > mvn clean package 

   this should download all the dependencies needed and pack everything in a jar file

Execution To execute our program, make sure you are in the project folder and run the following command in command line:

    To test this distributed network, first run the server class to start the server, then start testing the client:
    > java -cp target\BlockChain_FileSystem.jar ece465.server
    > java -cp target\BlockChain_FileSystem.jar ece465.client_test
    Follow the commanline instruction in client. 


## AWS Usage

Set up VPC and EC2

    sh ./AWS/create_ec2.sh
Deploy and install Java

    sh ./AWS/deploy.sh

Start server

    sh ./AWS/run.sh

Terminate EC2 and delete VPC

    sh ./AWS/terminate.sh




## Authors

- Allister Liu
- Victor Zhang

# Distributed File System
Cooper Union ECE-465 Spring 2021

![alt text](https://github.com/Victoooooor/ECE-465-Cloud-Computing/blob/main/ECE465_MVP4.png?raw=true)

    Shown above is a simple demostration of our distributed network. Each node server is connected to at least two other nodes, and a peer list of the nodes that it is connected to is maintained within each node. 

    When a new node server is added to this network, it will need an entry node IP address. The new node is registered to the entry point and its registration will be broadcasted to other nodes (to be implemented) so that the new node is connected to more than one nodes for redundancy purposes. 

## Pre-requsite: 

  - MariaDB (version 10.5 or newer) 
  - Maven
  - AWS CLI
  - ngrok (to host a node on your local machine)

## Local database setup:
  
  Make sure your mySQL server is running.
  
  If you know your root user password for mySQL server, run the following script and type in your password when asked.
  
    sh ./localDBsetup.sh
    
  If you would like to use another user for mySQL server, run the following commands and replace "root" with user of your choice, thne type in the corresponding password when asked.

    mysql -u root -p < sql_script\a.sql
    
    mysql -u root -p < sql_script\b.sql
    
## Building the program: 

  Run the following script to generate a .jar file.
    
    sh ./build.sh
    
## Runing a node on your local machine:

### Set up:

  To run a node on your local machine, you will need to host a server locally to interact with other nodes in the cloud. There are several ways to do this, we would recommend using ngrok:
  
  Please follow the following link to sign up and install.
  
    https://ngrok.com/download
    
  Run the command below to authenticate and connect to your account:
  
    ./ngrok authtoken <your_auth_token>
    
  Afterwards, please start a tcp server on your port 4567 by running the following:
    
    ./ngrok tcp 4567        (on MacOS and Linux)
    ngrok.exe tcp 4567      (on Windows)
    
  Then you will see a similar window like this![alt text](https://github.com/Victoooooor/ECE-465-Cloud-Computing/blob/frontend/ngrok.png?raw=true)
  
  
  #### **Please copy the portion of forwarding URL in the format of** 
  
    *.tcp.ngrok.io:*****
    
  #### **into ./selfip.txt (create if not exist)**
  

### Run:

  If you are on a MacOS machine, run the following script:
    
    sh ./run_macOS.sh
    
  If you are on a Windows machine, make sure you cd into the project folder and run the following .BAT script:
  
    run_Windows.BAT

  This will start a server on your machine launch a GUI window for you to interact with it.

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

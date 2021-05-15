# Distributed File System
Cooper Union ECE-465 Spring 2021

## P2P FileSystem Overview

![alt text](https://github.com/Victoooooor/ECE-465-Cloud-Computing/blob/main/images/architecture.png?raw=true)

- Peer-to-Peer (P2P) Systems have been the goto solution for maintaining internet freedom, it is also a popular distributed architecture for partition tasks and workloads between devices.

- p2p network has a wide variety of use cases including protocols like Napster and Spotify peer to peer music streaming service. Napster uses the p2p architecture to create a network of shared disk storage and is used for file sharing, and Spotify uses their client side p2p network for music streaming to offload significant amount of workload from their central server. Their evaluation of the p2p layer is published here https://ieeexplore.ieee.org/document/5569963 .

- Seeing that p2p can take advantage of the growing market of personal computing devices, this project aims to create a p2p Filesystem using a network of nodes similar to Napster and Gnutella with no ultrapeers/nodes.

- Each node is composed of a client and a server, the server is connected to the local disk storage database and is constantly listening for requests either from the network or from local client. All requests are treated equal and is transferred as a Json format string. The server parses the Json request and performs the requested action. The server and its subclasses are multithreaded to handle parallel requests as well as to speed up local storage lookup and transfer. The server uploads file from local device to the local designated storage (MariaDB), which is then accessible by the entire p2p network.

- The client on the other hand, is created to be as light-weight as possible with no computation in its process. It serves solely as a interface between the user and the server, generating correct Json request and send it to the correct destination IP through TCP serversocket. The current client supports three types of actions: adding to network, upload to network, and search on the entire network then fetch desired files.

- Since a shared network storage should not have access to the entire disk, MariaDB is chosen as local storage device, and accepts connection from local server using JDBC wrapped by Hikaricp connection pool.

![alt text](https://github.com/Victoooooor/ECE-465-Cloud-Computing/blob/main/images/ECE465_MVP4%20.png?raw=true)

   Shown above is a simple demostration of our distributed network. Each node server is connected to at least two other nodes, and a peer list of the nodes that it is connected to is maintained within each node.

   When a new node server is added to this network, it will need an entry node IP address. The new node is registered to the entry point and its registration will be broadcasted to other nodes (to be implemented) so that the new node is connected to more than one nodes for redundancy purposes.

## Pre-requsite:

  - MariaDB (version 10.5 or newer)
  - Maven
  - AWS CLI
  - ngrok (to host a node on your local machine)

## Local database setup:

  **Make sure your mySQL server is running.**

  - If you know your root user password for mySQL server, run the following script and type in your password when asked.

        sh ./localDBsetup.sh

  - If you would like to use another user for mySQL server, run the following commands and replace "root" with user of your choice, thne type in the corresponding password when asked.

        mysql -u root -p < sql_script\a.sql

        mysql -u root -p < sql_script\b.sql

## Building the program:

  - Run the following script to generate a .jar file.

        sh ./build.sh

## Runing a node on your local machine:

### Set up:

  To run a node on your local machine, you will need to host a server locally to interact with other nodes in the cloud. There are several ways to do this, we would recommend using ngrok:

  - Please follow the following link to sign up and install.

        https://ngrok.com/download

  - Run the command below to authenticate and connect to your account:

        ./ngrok authtoken <your_auth_token>

  - Afterwards, please start a tcp server on your port 4567 by running the following:

        ./ngrok tcp 4567        (on MacOS and Linux)
        ngrok.exe tcp 4567      (on Windows)

  - Then you will see a similar window like this![alt text](https://github.com/Victoooooor/ECE-465-Cloud-Computing/blob/main/images/ngrok.png?raw=true)


  - #### **Please copy the portion of forwarding URL in the format of**

        *.tcp.ngrok.io:*****

  - #### **into ./selfip.txt (create if not exist)**


### Run:

  - If you are on a MacOS machine, run the following script:

        sh ./run_macOS.sh

  - If you are on a Windows machine, make sure you cd into the project folder and run the following .BAT script:

        run_Windows.BAT

  This will start a server on your machine launch a GUI window for you to interact with it.

## AWS Usage

- Create VPC and EC2

      sh ./AWS/create_ec2.sh

- Create MariaDB in RDS

      sh ./AWS/create_rds.sh

- Deploy the program and install Java onto each EC2 instance, and make sure each EC2 instance is linked to a RDS database

      sh ./AWS/deploy.sh

- Start server

      sh ./AWS/run.sh

- Terminate EC2 and delete VPC

      sh ./AWS/terminate.sh




## Authors

- Allister Liu
- Victor Zhang

## Presentation(Video)

[Distributed Filesystem](https://youtu.be/tTnu_fIqGGc)

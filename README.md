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
    
    

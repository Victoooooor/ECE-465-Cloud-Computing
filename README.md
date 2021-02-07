# ECE-465-Cloud-Computing
Cooper Union ECE-465 Spring 2021

To run Threade File Storage:
    run a.sql and b.sql under "./sql script" folder for mariadb to setup database and user w/ privileges

Then run script.sh under "./testfile" which randomly generate small files for testing

Run ece465.threaded_store_test.java and ece465.single_store_test.java,
    the speed difference should be quite noticable
Run ece465.search_test.java in with 1 thread and 50 threads,
    the speed difference should be quite noticable

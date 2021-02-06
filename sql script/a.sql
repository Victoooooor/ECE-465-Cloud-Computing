/* initialize table */
CREATE DATABASE IF NOT EXISTS ece465;
USE ece465;

CREATE TABLE IF NOT EXISTS files (
    fid INT                     NOT NULL AUTO_INCREMENT,
    fname VARCHAR(320)          NOT NULL,
    stored LONGBLOB             DEFAULT NULL,
    bid INT,
    INDEX(fname),
    PRIMARY KEY(fid)
);

CREATE TABLE IF NOT EXISTS blocks (
    bid INT                     NOT NULL AUTO_INCREMENT,
    prev_hash VARCHAR(64)       DEFAULT NULL,
    curr_hash VARCHAR(64)       DEFAULT NULL,
    timest BIGINT               DEFAULT NULL,
    nonce INT,
    fid INT                     DEFAULT NULL,
    FOREIGN KEY(fid) REFERENCES files(fid) ON DELETE CASCADE,
    PRIMARY KEY(bid)
);

ALTER TABLE files
    ADD FOREIGN KEY(bid) REFERENCES blocks(bid) ON DELETE SET NULL;
#!/usr/bin/env bash

PROFILE=default
REGION=us-east-1
PREAMBLE="--profile ${PROFILE} --region ${REGION}"

VPC_CDR=10.0.0.0/16
PUBLIC_CDR=10.0.1.0/24
PRIVATE_CDR=10.0.2.0/24

APP_TYPE=type
APP_TYPE_NAME=distributed-app
APP_TAG_NAME=APP
APP_TAG_VALUE=distributedFileSystem

KEY_NAME=ece465_finalProject
KEY_FILE=~/.ssh/pems/${KEY_NAME}.pem

# for Amazon Linux 2 on x86_64
AMI_ID=ami-0915bcb5fa77e4892
INSTANCES_COUNT=2
INSTANCE_TYPE=t2.micro
USER=ec2-user

TARG="./target"
PROG="./target/BlockChain_FileSystem.jar"
CLASSPATH="ece465.server"

IPS_FILE="./ips.txt"
SELFIP_FILE="./selfip.txt"
ENV_FILE="./.env"


#!/usr/bin/env bash

source ./AWS/create_vpc.sh

NOW=$(date '+%Y%m%d%H%M%S')
LOGFILE="./logs/create_keypair-${NOW}.log"

echo "Running create_keypair.sh at ${NOW}" | tee -a ${LOGFILE}

aws ec2 create-key-pair ${PREAMBLE} --key-name ${KEY_NAME} --query 'KeyMaterial' --output text > ${KEY_FILE}
chmod 400 ${KEY_FILE}

echo "Done Creating Keypair." | tee -a ${LOGFILE}
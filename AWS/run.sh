#!/usr/bin/env bash

# fetch config from AWS for currently running infrastructure
source ./AWS/load_config.sh

NOW=$(date '+%Y%m%d%H%M%S')
LOGFILE="./logs/run-${NOW}.log"

echo "Running Full AWS infrastructure for ${APP_TAG_NAME}: ${APP_TAG_VALUE}" | tee ${LOGFILE}
echo "Running run.sh at ${NOW}" | tee -a ${LOGFILE}

# get public IP addresses of the instances (in the public subnet)
INSTANCES_IPS=$(aws ec2 describe-instances ${PREAMBLE} --filters Name=instance-state-name,Values=running Name=tag:${APP_TAG_NAME},Values=${APP_TAG_VALUE} --query 'Reservations[*].Instances[*].PublicIpAddress' --output text | tr -s '\t' ' ')
echo "Public IP addresses: ${INSTANCES_IPS}" | tee -a ${LOGFILE}

NODE=0
for host in ${INSTANCES_IPS}
do
    echo "Running ${PROG} at ${USER}@${host}:~/ ..." | tee -a ${LOGFILE}
    ssh -i ${KEY_FILE} ${USER}@${host} "java -cp ${PROG} ${CLASSPATH}" | tee -a ${LOGFILE} &
done

echo "Done." | tee -a ${LOGFILE}

exit 0

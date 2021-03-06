#!/usr/bin/env bash

# fetch config from AWS for currently running infrastructure
source ./AWS/load_config.sh

NOW=$(date '+%Y%m%d%H%M%S')
LOGFILE="./logs/deploy-${NOW}.log"

echo "Deploying Full AWS infrastructure for ${APP_TAG_NAME}: ${APP_TAG_VALUE}" | tee ${LOGFILE}
echo "Running deploy.sh at ${NOW}" | tee -a ${LOGFILE}

# get public IP addresses of the instances (in the public subnet)
INSTANCES_IPS=$(aws ec2 describe-instances ${PREAMBLE} --filters Name=instance-state-name,Values=running Name=tag:${APP_TAG_NAME},Values=${APP_TAG_VALUE} --query 'Reservations[*].Instances[*].PublicIpAddress' --output text | tr -s '\t' ' ')
echo "Public IP addresses: ${INSTANCES_IPS}" | tee -a ${LOGFILE}

i=0
for host in ${INSTANCES_IPS}
do
  # adds host to trusted ssh hosts so that it does not wait on request
  ssh-keyscan -H ${host} >> ~/.ssh/known_hosts | tee -a ${LOGFILE}
	echo "Copying over ${TARG} to ${USER}@${host}:~/ ..." | tee -a ${LOGFILE}
	scp -i ${KEY_FILE} -r ${TARG} ${USER}@${host}:~/ | tee -a ${LOGFILE}
	echo "Copying over ${IPS_FILE} to ${USER}@${host}:~/ ..." | tee -a ${LOGFILE}
	scp -i ${KEY_FILE} -r ${IPS_FILE} ${USER}@${host}:~/ | tee -a ${LOGFILE}
    
    echo "Copying over ${ENV_FILE} to ${USER}@${host}:~/ ..." | tee -a ${LOGFILE}
    scp -i ${KEY_FILE} -r ${ENV_FILE} ${USER}@${host}:~/ | tee -a ${LOGFILE}
	echo "Installing JDK to ${USER}@${host}:~/ ..." | tee -a ${LOGFILE}
	ssh -i ${KEY_FILE} ${USER}@${host} "sudo amazon-linux-extras install java-openjdk11"  | tee -a ${LOGFILE}
	#ssh -i ${KEY_FILE} ${USER}@${host} "mkdir FFTtmp"  | tee -a ${LOGFILE}
    #ssh -i ${KEY_FILE} ${USER}@${host} "echo 'hello' | tr -s ' ' '\n' > './selfip.txt'"  | tee -a ${LOGFILE}
    echo ${host} | tr -s ' ' '\n' > ${SELFIP_FILE}
    echo "Copying over ${SELFIP_FILE} to ${USER}@${host}:~/ ..." | tee -a ${LOGFILE}
    scp -i ${KEY_FILE} -r ${SELFIP_FILE} ${USER}@${host}:~/ | tee -a ${LOGFILE}
    echo "DB_PASS=PASSWORD\nDB_URL=jdbc:mysql://${DB_ENDPOINTS[$i]}/ece465" > ${ENV_FILE}
    echo "Copying over ${ENV_FILE} to ${USER}@${host}:~/ ..." | tee -a ${LOGFILE}
    scp -i ${KEY_FILE} -r ${ENV_FILE} ${USER}@${host}:~/ | tee -a ${LOGFILE}
    
    echo "Setting up mariadb on RDS" | tee -a ${LOGFILE}
    mysql -h ${DB_ENDPOINTS[$i]} -u root --password=PASSWORD < ./sql_script/a.sql
    mysql -h ${DB_ENDPOINTS[$i]} -u root --password=PASSWORD < ./sql_script/b.sql
    ((i=i+1))
done

echo "Done." | tee -a ${LOGFILE}

exit 0

#!/usr/bin/env bash

source ./AWS/create_keypair.sh

NOW=$(date '+%Y%m%d%H%M%S')
LOGFILE="./logs/create_ec2-${NOW}.log"

echo "Running create_ec2_in_vpc.sh at ${NOW}" | tee -a ${LOGFILE}

# create the security group
echo "Create the security group" | tee -a ${LOGFILE}
SEC_GROUP_ID=$(aws ec2 create-security-group ${PREAMBLE} --group-name AccessRules --description "Security group for specific accesses" --vpc-id ${VPC_ID} --tag-specifications ResourceType=security-group,"Tags=[{Key=${APP_TYPE},Value=${APP_TYPE_NAME}},{Key=${APP_TAG_NAME},Value=${APP_TAG_VALUE}}]" | jq '.GroupId' | tr -d '"')

# set the security group for ingress from the Internet
echo "Set the security group for ingress from the Internet" | tee -a ${LOGFILE}
aws ec2 authorize-security-group-ingress ${PREAMBLE} --group-id ${SEC_GROUP_ID} --protocol tcp --port 22 --cidr 0.0.0.0/0 | tee -a ${LOGFILE}
aws ec2 authorize-security-group-ingress ${PREAMBLE} --group-id ${SEC_GROUP_ID} --protocol tcp --port 4567 --cidr 0.0.0.0/0 | tee -a ${LOGFILE}
aws ec2 authorize-security-group-ingress ${PREAMBLE} --group-id ${SEC_GROUP_ID} --protocol tcp --port 6969 --cidr 0.0.0.0/0 | tee -a ${LOGFILE}

# create the instance(s) with tags
echo "Create ${INSTANCES_COUNT} instances" | tee -a ${LOGFILE}
aws ec2 run-instances ${PREAMBLE} --image-id ${AMI_ID} --count ${INSTANCES_COUNT} --instance-type ${INSTANCE_TYPE} --key-name ${KEY_NAME} --security-group-ids ${SEC_GROUP_ID} --subnet-id ${SN_ID_PUBLIC} \
   --tag-specifications ResourceType=instance,"Tags=[{Key=${APP_TYPE},Value=${APP_TYPE_NAME}},{Key=${APP_TAG_NAME},Value=${APP_TAG_VALUE}}]" \
   ResourceType=volume,"Tags=[{Key=${APP_TYPE},Value=${APP_TYPE_NAME}},{Key=${APP_TAG_NAME},Value=${APP_TAG_VALUE}}]" | tee -a ${LOGFILE}
# takes up to 90 seconds to set up EC2 instance
sleep 90

# get instances IDs for those running and are part of tags
echo "Fetch instances IDs" | tee -a ${LOGFILE}
INSTANCES_IDS=$(aws ec2 describe-instances ${PREAMBLE} --filters Name=instance-state-name,Values=running Name=tag:${APP_TAG_NAME},Values=${APP_TAG_VALUE} --query 'Reservations[*].Instances[*].InstanceId' --output text | tr -s '\t' ' ')
echo "Instances IDs: ${INSTANCES_IDS}" | tee -a ${LOGFILE}

# get public IP addresses of the instances (in the public subnet)
echo "Fetch public IP addresses of the instances" | tee -a ${LOGFILE}
INSTANCES_IPS=$(aws ec2 describe-instances ${PREAMBLE} --filters Name=instance-state-name,Values=running Name=tag:${APP_TAG_NAME},Values=${APP_TAG_VALUE} --query 'Reservations[*].Instances[*].PublicIpAddress' --output text | tr -s '\t' ' ')
echo ${INSTANCES_IPS} | tr -s ' ' '\n' > ${IPS_FILE}

echo "Public IP addresses: ${INSTANCES_IPS}" | tee -a ${LOGFILE}

echo "Done." | tee -a ${LOGFILE}

exit 0
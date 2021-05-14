#!/usr/bin/env bash

source ./AWS/config.sh

NOW=$(date '+%Y%m%d%H%M%S')
LOGFILE="./logs/create_rds-${NOW}.log"

echo "Running create_rds_in_vpc.sh at ${NOW}" | tee -a ${LOGFILE}


# create parameter group
echo "Create the parameter group" | tee -a ${LOGFILE}
PARAM_GROUP_NAME=$(aws rds create-db-parameter-group ${PREAMBLE} --db-parameter-group-name ${DB_PARAMETER_GROUP_NAME} --db-parameter-group-family ${DB_PARAMETER_GROUP_FAMILY} --description "Parameter group for mariaDB 10.4 in AWS RDS" --tags "
        [
          {
            \"Key\":\"${APP_TYPE}\",
            \"Value\":\"${APP_TYPE_NAME}\"
          },
          {
            \"Key\":\"${APP_TAG_NAME}\",
            \"Value\":\"${APP_TAG_VALUE}\"
          }
        ]" | jq '.DBParameterGroup.DBParameterGroupName' | tr -d '"')
        
echo "Created parameter group ${PARAM_GROUP_NAME}" | tee -a ${LOGFILE}

echo "Modify parameter group ${PARAM_GROUP_NAME}" | tee -a ${LOGFILE}
aws rds modify-db-parameter-group ${PREAMBLE} --db-parameter-group-name ${DB_PARAMETER_GROUP_NAME} --parameters "ParameterName='max_allowed_packet',ParameterValue=1073741824,ApplyMethod=immediate"
echo "Modified parameter group ${DB_PARAMETER_GROUP_NAME}" | tee -a ${LOGFILE}

for i in $(seq 1 1 ${INSTANCES_COUNT});
do
    echo "Create databse number ${i}" | tee -a ${LOGFILE};
        aws rds create-db-instance ${PREAMBLE} \
        --db-name ${DB_NAME} \
        --db-instance-identifier "${DB_NAME}-${i}" \
        --allocated-storage 20 \
        --db-instance-class ${DB_INSTANCE_CLASS} \
        --engine ${DB_ENGINE} \
        --master-username root \
        --master-user-password PASSWORD \
        --db-parameter-group-name ${DB_PARAMETER_GROUP_NAME} \
        --no-multi-az \
        --engine-version ${DB_ENGINE_VERSION} \
        --publicly-accessible \
        --max-allocated-storage 50 \
    echo "Waiting 2 minutes for databse to create..."
    sleep 120
done

declare -a DB_ENDPOINTS

for i in $(seq 1 1 ${INSTANCES_COUNT});
do
     echo "Fetch endpoint of databse number ${i}" | tee -a ${LOGFILE};
    DB_ENDPOINT=$(aws rds describe-db-instances \
    --db-instance-identifier "${DB_NAME}-${i}" | jq '.DBInstances[].Endpoint.Address' | tr -d '"')
    DB_ENDPOINTS+=(${DB_ENDPOINT})
    
    if [ $i -lt $INSTANCES_COUNT ]
    then
        echo "Waiting 90 seconds..."
        sleep 90
    fi
    
done

echo ${DB_ENDPOINTS[*]}

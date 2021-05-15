#!/bin/bash

# First ensure dependencies loaded since .m2 may be empty
echo "Checking APP dependencies..."
mvn dependency:tree
mvn help:evaluate -Dexpression=project.version
echo "Done."

# Clean repo from builds
./clean.sh

#
# Config
#
APP_VERSION_FILE=./app.version

THEUSER=$(/usr/bin/whoami)

# Remove white space and periods from user name
THEUSER=${THEUSER// /}
THEUSER=${THEUSER//./}

NOW=$(date "+%Y%m%d%H%M%S")
APP_MAVEN_VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -e '^[^\[]')
APP_GIT_VERSION=$(git rev-parse --abbrev-ref HEAD)

APP_VERSION=${APP_MAVEN_VERSION}-${APP_GIT_VERSION}_${NOW}_${THEUSER}
echo ${APP_VERSION} > ${APP_VERSION_FILE}

echo "LOCALLY building runtime to local folder: ./target ..."
echo "Version = ${APP_VERSION}"

if [ ! -f "${APP_VERSION_FILE}" ]; then
    echo "APP Version file DOES NOT exist. CANNOT proceed with build."
    exit 1
fi

#
# Config
#
APP_VERSION=$(cat ${APP_VERSION_FILE})

# mvn install
mvn install -Dmaven.test.skip=true

echo "Done building APP."
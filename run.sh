#!/bin/bash

# NOTE: The run.sh file (ucef-gateway/test-federation/run.sh) was modified from the run.sh file (ucef-gateway/test-federation/run.sh) that was downloaded as part of the ucef-gateway repository.
#   The ucef-gateway repository was cloned from here: https://github.com/usnistgov/ucef-gateway.git
#   The code on the feature/refactor branch of the ucef-gateway repository was used.
#
# The License of the ucef-gateway repository has been copied in the "Third Party License" directory of this repo (file name is "LICENSE").
#
# Copyright (c) 2017-2018,
# Alliance for Sustainable Energy, LLC
# All rights reserved.



root_directory=`pwd`
logs_directory=$root_directory/logs

timestamp=`date +"%F_%T"`

nc -z 127.0.0.1 8083
if [ $? -eq 0 ]; then
    echo Cannot start the federation manager on port 8083
    exit 1
fi

if [ ! -d $logs_directory ]; then
    echo Creating the $logs_directory directory
    mkdir $logs_directory
fi

# run the federation manager
cd $root_directory/GatewayTest_deployment
xterm -fg white -bg black -l -lf $logs_directory/federation-manager-${timestamp}.log -T "Federation Manager" -geometry 140x40+0+0 -e "export CPSWT_ROOT=`pwd` && mvn exec:java -P FederationManagerExecJava" &

printf "Waiting for the federation manager to come online.."
until $(curl -o /dev/null -s -f -X GET http://127.0.0.1:8083/fedmgr); do
    printf "."
    sleep 5
done
printf "\n"

curl -o /dev/null -s -X POST http://127.0.0.1:8083/fedmgr --data '{"action": "START"}' -H "Content-Type: application/json"

# run the other federates
cd $root_directory/GatewayTest_deployment
xterm -fg orange -bg black -l -lf $logs_directory/test-federate-${timestamp}.log -T "Test Federate" -geometry 140x40+180+60 -e "mvn exec:java -P TestFederate,ExecJava" &

cd $root_directory/ExampleGateway/target
cp $root_directory/ExampleGateway/RTI.rid .
cp -r $root_directory/ExampleGateway/conf/ .
xterm -fg cyan -bg black -l -lf $logs_directory/helics-gateway-implementation-${timestamp}.log -T "Helics Gateway Implementation" -geometry 140x40+360+120 -e "java -Djava.library.path=classes/ -Dlog4j.configurationFile=conf/log4j2.xml -Djava.net.preferIPv4Stack=true -jar HelicsGatewayImplementation-0.0.1-SNAPSHOT.jar conf/HelicsGatewayImplementation.json" &

# terminate the simulation
read -n 1 -r -s -p "Press any key to terminate the federation execution..."
printf "\n"

curl -o /dev/null -s -X POST http://127.0.0.1:8083/fedmgr --data '{"action": "TERMINATE"}' -H "Content-Type: application/json"

printf "Waiting for the federation manager to terminate.."
while $(curl -o /dev/null -s -f -X GET http://127.0.0.1:8083/fedmgr); do
    printf "."
    sleep 5
done
printf "\n"


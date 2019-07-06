#!/bin/bash

#TODO: Add base back in if it becomes relevant
#MODULES="cauldron commons config datapack logging player-events"
MODULES="player-events logging config datapack commons cauldron"


for MODULE in $MODULES;
do
	MODULE_NAME="modules/cotton-$MODULE"
	cd $MODULE_NAME
	../../gradlew clean build publishToMavenLocal
	cd ../..
done

./gradlew clean build

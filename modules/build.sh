#!/bin/bash

#TODO: Add base back in if it becomes relevant
#MODULES="cauldron commons config datapack logging player-events"
MODULES="player-events logging config datapack commons cauldron"


for MODULE in $MODULES;
do
	MODULE_NAME="cotton-$MODULE"
	cd $MODULE_NAME
	gradle clean build
	cd ..
done
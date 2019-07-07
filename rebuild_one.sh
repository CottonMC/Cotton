#!/bin/bash

MODULE_NAME="modules/cotton-$@"
cd $MODULE_NAME
../../gradlew clean build publishToMavenLocal
cd ../..

./gradlew clean build

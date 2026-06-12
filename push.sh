#!/bin/sh
VER=v1.0
REPO=gvensly
NAME=cocotanaa
./gradlew -Pprod bootJar jib -Djib.to.image=$REPO/$NAME:$VER && docker push $REPO/$NAME:$VER
echo $REPO/$NAME:$VER
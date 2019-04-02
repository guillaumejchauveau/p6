#!/usr/bin/env bash

cd $(dirname $0)/../..
chmod +x ./gradlew
./gradlew cleanDist
